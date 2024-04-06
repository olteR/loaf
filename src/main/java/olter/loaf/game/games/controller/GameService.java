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
import olter.loaf.game.games.exception.NotInGameException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobby.lobbies.exception.NotInLobbyException;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.lobby.lobbies.model.LobbyRepository;
import olter.loaf.users.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ConfigRepository configRepository;
    private final LobbyRepository lobbyRepository;
    private final GameMapper gameMapper;

    private final Integer STARTING_GOLD = 2;
    private final Integer STARTING_CARDS = 4;

    public GameEntity createGameForLobby(String code) {
        log.info("Creating game for lobby " + code);
        GameEntity game = new GameEntity();
        game.setPhase(GamePhaseEnum.NOT_STARTED);
        game.setUniqueDistricts(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters());
        gameRepository.save(game);
        return game;
    }


    public void startGame(LobbyEntity lobby) {
        log.info("Starting game for lobby " + lobby.getCode());
        Random r = new Random();
        GameEntity game = lobby.getGame();

        if (game.getCrownedPlayer() == null) {
            game.setCrownedPlayer(lobby.getMembers().get(r.nextInt(lobby.getMembers().size())).getId());
        }

        game.setTurn(1);
        game.setPhase(GamePhaseEnum.SELECTION);
        game.setCurrentPlayer(game.getCrownedPlayer());
        game.setDeck(assembleDeck(game.getUniqueDistricts()));

        gameRepository.save(game);
        playerRepository.saveAll(lobby.getMembers().stream()
            .map(
                user ->
                    new PlayerEntity(
                        user.getId(),
                        STARTING_GOLD,
                        null,
                        false,
                        drawFromDeck(game, STARTING_CARDS),
                        Collections.emptyList(),
                        game))
            .toList());
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

    public GameStateResponse getGameState(Long id, UserEntity loggedInUser) {
        log.info("Getting " + id + " state for " + loggedInUser.getName());
        GameEntity game =
            gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), id));
        PlayerEntity player =
            playerRepository.findFirstByUserIdAndGame(loggedInUser.getId(), game)
                .orElseThrow(() -> new NotInGameException(game.getId(), loggedInUser.getId()));

        game.getPlayers().forEach(p -> {
            if (!p.getIsRevealed()) {
                p.setCurrentCharacter(null);
            }
        });

        return gameMapper.entitiesToStateResponse(game, player);
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

    private List<Long> getDefaultUniqueDistricts() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT).stream()
            .map(ConfigEntity::getConfigId)
            .toList();
    }

    private List<Long> getDefaultCharacters() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_CHARACTER).stream()
            .map(ConfigEntity::getConfigId)
            .toList();
    }
}
