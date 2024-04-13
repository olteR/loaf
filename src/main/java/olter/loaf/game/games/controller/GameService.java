package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
import olter.loaf.game.games.GameMapper;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameStateResponse;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.exception.InvalidPhaseActionException;
import olter.loaf.game.games.exception.NotInGameException;
import olter.loaf.game.games.exception.NotOnTurnException;
import olter.loaf.game.games.model.GameCharacterEmbeddable;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.logs.model.LogEntity;
import olter.loaf.game.logs.model.LogRepository;
import olter.loaf.game.logs.model.LogTypeEnum;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobby.lobbies.exception.NotInLobbyException;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.lobby.lobbies.model.LobbyRepository;
import olter.loaf.users.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ConfigRepository configRepository;
    private final LobbyRepository lobbyRepository;
    private final LogRepository logRepository;
    private final GameMapper gameMapper;

    private final Integer STARTING_GOLD = 2;
    private final Integer STARTING_CARDS = 4;

    public GameEntity createGameForLobby(LobbyEntity lobby) {
        log.info("Creating game for lobby " + lobby.getCode());
        GameEntity game = new GameEntity();
        game.setPhase(GamePhaseEnum.NOT_STARTED);
        game.setUniqueDistricts(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters(lobby.getMaxMembers() == 8));
        gameRepository.save(game);

        PlayerEntity p = new PlayerEntity();
        p.setUserId(lobby.getOwner());
        p.setOrder(1);
        p.setGame(game);
        playerRepository.save(p);
        return game;
    }


    public void startGame(LobbyEntity lobby) {
        log.info("Starting game for lobby " + lobby.getCode());
        Random r = new Random();
        GameEntity game = lobby.getGame();
        int gameSize = lobby.getMembers().size();

        if (game.getCrownedPlayer() == null) {
            game.setCrownedPlayer(lobby.getMembers().get(r.nextInt(lobby.getMembers().size())).getId());
        }

        game.setTurn(1);
        game.setPhase(GamePhaseEnum.SELECTION);
        game.setCurrentPlayer(game.getCrownedPlayer());
        game.setDeck(assembleDeck(game.getUniqueDistricts()));
        game.setDownwardDiscard(r.nextInt(game.getCharacters().size()));
        game.setUpwardDiscard(discardCharacters(configRepository.findByTypeAndConfigId(
            game.getCharacters().size() == 9 ? ConfigTypeEnum.UPWARDS_CARDS_9C : ConfigTypeEnum.UPWARDS_CARDS_8C,
            (long) gameSize).getConfigValue(), gameSize, List.of(game.getDownwardDiscard(), 4)));

        gameRepository.save(game);
        playerRepository.saveAll(playerRepository.findAllByGame(game).stream().peek(p -> {
            p.setGold(STARTING_GOLD);
            p.setHand(drawFromDeck(game, STARTING_CARDS));
        }).toList());
    }

    public GameDetailsResponse getGame(String code, UserEntity loggedInUser) {
        log.info("Getting lobby " + code + " for " + loggedInUser.getName());
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        if (!lobby.getMembers().contains(loggedInUser)) {
            throw new NotInLobbyException(lobby.getId(), loggedInUser.getId());
        }
        return gameMapper.lobbyToDetailsResponse(lobby);
    }

    public GameStateResponse getGameState(String code, UserEntity loggedInUser) {
        log.info("Getting " + code + " state for " + loggedInUser.getName());
        GameEntity game =
            gameRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));
        PlayerEntity player =
            playerRepository.findByUserIdAndGame(loggedInUser.getId(), game)
                .orElseThrow(() -> new NotInGameException(game.getId(), loggedInUser.getId()));

        List<Integer> unavailableCharacters = new ArrayList<>();
        if (game.getPhase() == GamePhaseEnum.SELECTION && game.getCurrentPlayer().equals(loggedInUser.getId())) {
            unavailableCharacters.add(game.getDownwardDiscard());
            unavailableCharacters.addAll(game.getPlayers().stream().map(PlayerEntity::getCurrentCharacter).filter(
                Objects::nonNull).toList());
        }

        game.getPlayers().forEach(p -> {
            if (!p.getIsRevealed()) {
                p.setCurrentCharacter(null);
            }
        });

        GameStateResponse response = gameMapper.entitiesToStateResponse(game, player);
        response.setUnavailableCharacters(unavailableCharacters);

        return response;
    }

    public void selectCharacter(String code, Integer selectedCharacter, UserEntity loggedInUser) {
        log.info("Selecting character " + selectedCharacter + " for " + loggedInUser.getName() + " in game " + code);
        GameEntity game =
            gameRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));

        game.getPlayers().stream().filter(p -> p.getUserId().equals(loggedInUser.getId())).findFirst()
            .orElseThrow(() -> new NotInGameException(game.getId(),
                loggedInUser.getId())).setCurrentCharacter(selectedCharacter);
        validateSelectionTurn(game, loggedInUser.getId());
        setNextPlayer(game);
        gameRepository.save(game);

        // TODO: BROADCAST ON WS

        LogEntity log = new LogEntity();
        log.setGameId(game.getId());
        log.setUserId(loggedInUser.getId());
        log.setTarget(String.valueOf(
            game.getCharacters().stream().filter(c -> c.getNumber().equals(selectedCharacter)).findFirst()
                .orElseThrow(() -> new CorruptedGameException(game.getId())).getCharacterId()));
        log.setTurn(game.getTurn());
        log.setLogType(LogTypeEnum.SELECT_CHARACTER);
        logRepository.save(log);
    }

    private List<Long> drawFromDeck(GameEntity game, int cardCount) {
        List<Long> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(game.getDeck().remove(0));
        }
        return drawnCards;
    }

    private List<Long> assembleDeck(List<Long> uniqueDistricts) {
        return shuffleDeck(
            Stream.concat(assembleBaseDeck().stream(), uniqueDistricts.stream()).toList());
    }

    private List<Long> shuffleDeck(List<Long> cards) {
        List<Long> deck = new ArrayList<>(cards);
        Collections.shuffle(deck);
        return deck;
    }

    private List<Long> assembleBaseDeck() {
        List<Long> baseDeck = new ArrayList<>();
        configRepository
            .findAllByType(ConfigTypeEnum.BASE_CARD)
            .forEach(
                configEntity -> {
                    for (int i = 0; i < configEntity.getConfigValue(); ++i) {
                        baseDeck.add(configEntity.getConfigId());
                    }
                });
        return baseDeck;
    }

    private List<Integer> discardCharacters(int discardCount, int characters, List<Integer> excludes) {
        Random r = new Random();
        List<Integer> discardedCharacters = new ArrayList<>();
        while (discardedCharacters.size() < discardCount) {
            Integer chosenCharacter = r.nextInt(characters) + 1;
            if (!excludes.contains(chosenCharacter)) {
                discardedCharacters.add(chosenCharacter);
            }
        }
        return discardedCharacters;
    }

    private List<Long> getDefaultUniqueDistricts() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT).stream()
            .map(ConfigEntity::getConfigId)
            .toList();
    }

    private List<GameCharacterEmbeddable> getDefaultCharacters(boolean hasEightMaxPlayers) {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_CHARACTER).stream().filter(c -> {
                if (!hasEightMaxPlayers) {
                    return c.getConfigValue() != 9;
                }
                return true;
            })
            .map(gameMapper::configToCharacterEmbeddable)
            .toList();
    }

    private void setNextPlayer(GameEntity game) {
        PlayerEntity currentPlayer =
            game.getPlayers().stream().filter(p -> p.getUserId().equals(game.getCurrentPlayer())).findFirst()
                .orElseThrow(() -> new CorruptedGameException(game.getId()));

        if (currentPlayer.getOrder() < game.getPlayers().size()) {
            game.setCurrentPlayer(game.getPlayers().get(game.getPlayers().indexOf(currentPlayer) + 1).getUserId());
        } else {
            game.setCurrentPlayer(game.getPlayers().get(0).getUserId());
            switch (game.getPhase()) {
                case SELECTION -> game.setPhase(GamePhaseEnum.TURN);
                case TURN -> game.setPhase(GamePhaseEnum.SELECTION);
                case END_TURN -> game.setPhase(GamePhaseEnum.ENDED);
            }
        }
    }

    private void validateSelectionTurn(GameEntity game, Long userId) {
        if (!Objects.equals(game.getPhase(), GamePhaseEnum.SELECTION)) {
            throw new InvalidPhaseActionException(game.getId());
        }
        if (!Objects.equals(game.getCurrentPlayer(), userId)) {
            throw new NotOnTurnException(game.getId(), userId);
        }
    }
}
