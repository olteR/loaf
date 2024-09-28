package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.cards.CardMapper;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.CharacterRepository;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.cards.model.DistrictRepository;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
import olter.loaf.game.games.GameMapper;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameUpdateDto;
import olter.loaf.game.games.dto.GameUpdateTypeEnum;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.exception.InvalidPhaseActionException;
import olter.loaf.game.games.exception.NotInGameException;
import olter.loaf.game.games.exception.NotOnTurnException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.game.logs.controller.LogService;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.users.model.UserEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ConfigRepository configRepository;
    private final LogService logService;
    private final GameMapper gameMapper;
    private final CardMapper cardMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Integer STARTING_GOLD = 2;
    private final Integer STARTING_CARDS = 4;
    private final CharacterRepository characterRepository;
    private final DistrictRepository districtRepository;

    public GameEntity createGameForLobby(LobbyEntity lobby) {
        log.info("Creating game for lobby {}", lobby.getCode());
        GameEntity game = new GameEntity();
        game.setPhase(GamePhaseEnum.NOT_STARTED);
        game.setUniqueDistricts(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters(lobby.getMaxMembers() == 8));
        gameRepository.save(game);

        PlayerEntity p = new PlayerEntity();
        p.setUserId(lobby.getOwner());
        p.setOrder(1);
        p.setGame(game);
        p.setIsRevealed(false);
        playerRepository.save(p);
        return game;
    }


    public void startGame(LobbyEntity lobby) {
        log.info("Starting game for lobby {}", lobby.getCode());
        Random r = new Random();
        GameEntity game = lobby.getGame();
        int gameSize = lobby.getMembers().size();

        if (game.getCrownedPlayer() == null) {
            game.setCrownedPlayer(game.getPlayers().get(r.nextInt(gameSize)));
        }
        Map<Integer, Integer> orderMap = assembleOrderMap(game.getCrownedPlayer().getOrder(), gameSize);

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
            p.setOrder(orderMap.get(p.getOrder()));
            if (p.getId().equals(game.getCurrentPlayer().getId())) {
                p.setUnavailableCharacters(new ArrayList<>(Collections.singletonList(game.getDownwardDiscard())));
            }
        }).toList());
    }

    public GameDetailsResponse getGame(String code, UserEntity loggedInUser) {
        log.info("Getting game details of {} for {}", code, loggedInUser.getName());
        GameEntity game =
            gameRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));
        PlayerEntity player =
            playerRepository.findByUserIdAndGame(loggedInUser.getId(), game)
                .orElseThrow(() -> new NotInGameException(game.getId(), loggedInUser.getId()));

        game.getPlayers().forEach(p -> {
            if (!p.getIsRevealed() && !p.getUserId().equals(loggedInUser.getId())) {
                p.setCurrentCharacter(null);
            }
        });

        return gameMapper.entityToDetailsResponse(game, player);
    }

    public List<Integer> selectCharacter(String code, Integer selectedCharacter, UserEntity loggedInUser) {
        log.info("Selecting character {} for {} in game {}", selectedCharacter, loggedInUser.getName(), code);
        GameEntity game =
            gameRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));

        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.SELECTION);
        List<Integer> skippedCharacters = IntStream.rangeClosed(1, game.getCharacters().size()).boxed().filter(c ->
            !game.getDownwardDiscard().equals(c) && !game.getUpwardDiscard().contains(c) &&
                !selectedCharacter.equals(c) && !game.getCurrentPlayer().getUnavailableCharacters().contains(c)
        ).collect(Collectors.toList());

        game.getCurrentPlayer().setCurrentCharacter(selectedCharacter);
        game.getCurrentPlayer().setSkippedCharacters(skippedCharacters);

        logService.logCharacterSelection(game, game.getCurrentPlayer());

        setNextPlayer(game);
        game
            .getPlayers()
            .forEach(
                p -> {
                    log.info("Broadcasting next player to {}", p.getUserId());
                    if (game.getCurrentPlayer().getId().equals(p.getId())) {
                        List<Integer> unavailableCharacters = new ArrayList<>(
                            game.getPlayers().stream().map(PlayerEntity::getCurrentCharacter).filter(
                                Objects::nonNull).toList());
                        unavailableCharacters.add(game.getDownwardDiscard());
                        p.setUnavailableCharacters(unavailableCharacters);
                        simpMessagingTemplate.convertAndSendToUser(
                            String.valueOf(p.getUserId()), "/topic/game/update",
                            new GameUpdateDto(GameUpdateTypeEnum.PLAYER_TURN, unavailableCharacters));
                    } else {
                        simpMessagingTemplate.convertAndSendToUser(
                            String.valueOf(p.getUserId()), "/topic/game/update",
                            new GameUpdateDto(GameUpdateTypeEnum.NEXT_PLAYER, game.getCurrentPlayer().getId()));
                    }
                });
        gameRepository.save(game);
        return skippedCharacters;
    }

    public List<DistrictResponse> gatherResources(String code, ResourceTypeEnum resource, UserEntity loggedInUser) {
        log.info("Selecting resource {} for {} in game {}", resource, loggedInUser.getName(), code);
        GameEntity game =
            gameRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));

        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.RESOURCE);
        if (resource.equals(ResourceTypeEnum.GOLD)) {
            game.getCurrentPlayer().setGold(game.getCurrentPlayer().getGold() + 2);
            game.setPhase(GamePhaseEnum.TURN);
            gameRepository.save(game);
        } else if (resource.equals(ResourceTypeEnum.CARDS)) {
            game.getCurrentPlayer().setDrawnCards(drawFromDeck(game, 2));
            gameRepository.save(game);
            return game.getCurrentPlayer().getDrawnCards().stream().map(cardMapper::entityToResponse).toList();
        }
        return Collections.emptyList();
    }

    private List<DistrictEntity> drawFromDeck(GameEntity game, int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(game.getDeck().remove(0));
        }
        return drawnCards;
    }

    private List<DistrictEntity> assembleDeck(List<Long> uniqueDistricts) {
        Map<Long, DistrictEntity> districts =
            districtRepository.findAll().stream().collect(Collectors.toMap(DistrictEntity::getId, d -> d));
        return shuffleDeck(
            Stream.concat(assembleBaseDeck().stream(), uniqueDistricts.stream()).map(districts::get).toList());
    }

    private List<DistrictEntity> shuffleDeck(List<DistrictEntity> cards) {
        List<DistrictEntity> deck = new ArrayList<>(cards);
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

    private List<CharacterEntity> getDefaultCharacters(boolean hasEightMaxPlayers) {
        return characterRepository.findAllByIdIn(
            configRepository.findAllByType(ConfigTypeEnum.DEFAULT_CHARACTER).stream().filter(c -> {
                    if (!hasEightMaxPlayers) {
                        return c.getConfigValue() != 9;
                    }
                    return true;
                })
                .map(ConfigEntity::getConfigId)
                .toList());
    }

    private void setNextPlayer(GameEntity game) {
        switch (game.getPhase()) {
            case SELECTION -> {
                if (game.getCurrentPlayer().getOrder() < game.getPlayers().size()) {
                    game.setCurrentPlayer(
                        game.getPlayers().get(game.getPlayers().indexOf(game.getCurrentPlayer()) + 1));
                } else {
                    Integer firstChar =
                        game.getPlayers().stream().map(PlayerEntity::getCurrentCharacter).min(Integer::compareTo).get();
                    PlayerEntity nextPlayer =
                        game.getPlayers().stream().filter(p -> p.getCurrentCharacter().equals(firstChar)).findFirst()
                            .orElseThrow(() -> new CorruptedGameException(game.getId()));

                    nextPlayer.setIsRevealed(true);
                    game.setCurrentPlayer(nextPlayer);
                    game.setPhase(GamePhaseEnum.RESOURCE);
                }
            }
            default -> log.warn("Unhandled phase {}", game.getPhase());
        }
    }

    private void validateGameTurn(GameEntity game, Long userId, GamePhaseEnum phase) {
        if (!Objects.equals(game.getPhase(), phase)) {
            throw new InvalidPhaseActionException(game.getId());
        }
        if (!Objects.equals(game.getCurrentPlayer().getUserId(), userId)) {
            throw new NotOnTurnException(game.getId(), userId);
        }
    }

    private Map<Integer, Integer> assembleOrderMap(Integer startingOrder, Integer players) {
        int orderCounter = startingOrder;
        Map<Integer, Integer> orderMap = new HashMap<>();
        for (int i = 1; i <= players; i++) {
            orderMap.put(orderCounter, i);
            if (orderCounter == players) {
                orderCounter = 1;
            } else {
                orderCounter++;
            }
        }
        return orderMap;
    }
}
