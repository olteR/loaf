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
import olter.loaf.game.games.dto.*;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.exception.InvalidPhaseActionException;
import olter.loaf.game.games.exception.NotInGameException;
import olter.loaf.game.games.exception.NotOnTurnException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.game.players.PlayerMapper;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobbies.model.LobbyEntity;
import olter.loaf.statistics.LogService;
import olter.loaf.users.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
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
    private final CharacterRepository characterRepository;
    private final DistrictRepository districtRepository;
    private final LogService logService;
    private final GameMapper gameMapper;
    private final CardMapper cardMapper;
    private final PlayerMapper playerMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${loaf.config.starting-gold}")
    private Integer STARTING_GOLD;
    @Value("${loaf.config.starting-cards}")
    private Integer STARTING_CARDS;
    @Value("${loaf.config.resource-gold}")
    private Integer RESOURCE_GOLD;

    public GameEntity createGameForLobby(LobbyEntity lobby) {
        log.info("Creating game for lobby {}", lobby.getCode());
        GameEntity game = new GameEntity();
        game.setPhase(GamePhaseEnum.NOT_STARTED);
        game.setUniqueDistricts(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters(lobby.getMaxMembers() == 8));
        game.setTurn(0);
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
        game.setDeck(assembleDeck(game.getUniqueDistricts()));
        game.setPlayers(game.getPlayers().stream().peek(p -> {
            p.setGold(STARTING_GOLD);
            p.setHand(drawFromDeck(game, STARTING_CARDS));
        }).collect(Collectors.toList()));

        startSelectionPhase(game);
        gameRepository.save(game);
    }

    public GameDetailsResponse getGame(String code, UserEntity loggedInUser) {
        log.info("Getting game details of {} for {}", code, loggedInUser.getName());
        GameEntity game = findGame(code);

        PlayerEntity player = playerRepository.findByUserIdAndGame(loggedInUser.getId(), game)
            .orElseThrow(() -> new NotInGameException(code, loggedInUser.getId()));

        game.getPlayers().forEach(p -> {
            if (!p.getIsRevealed() && !p.getUserId().equals(loggedInUser.getId())) {
                p.setCurrentCharacter(null);
            }
        });

        return gameMapper.entityToDetailsResponse(game, player, code);
    }

    public List<Integer> selectCharacter(String code, Integer selectedCharacter, UserEntity loggedInUser) {
        log.info("Selecting character {} for {} in game {}", selectedCharacter, loggedInUser.getName(), code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.SELECTION);

        List<Integer> skippedCharacters = IntStream.rangeClosed(1, game.getCharacters().size()).boxed().filter(
                c -> !game.getDownwardDiscard().equals(c) && !game.getUpwardDiscard().contains(c) &&
                    !selectedCharacter.equals(c) && !game.getCurrentPlayer().getUnavailableCharacters().contains(c))
            .collect(Collectors.toList());

        game.getCurrentPlayer().setCurrentCharacter(selectedCharacter);
        game.getCurrentPlayer().setSkippedCharacters(skippedCharacters);

        logService.logCharacterSelection(game);

        setNextPlayer(game);
        if (game.getPhase().equals(GamePhaseEnum.SELECTION)) {
            game.getPlayers().forEach(p -> {
                log.info("Broadcasting next player to {}", p.getUserId());
                if (game.getCurrentPlayer().getId().equals(p.getId())) {
                    List<Integer> unavailableCharacters = new ArrayList<>(
                        game.getPlayers().stream().map(PlayerEntity::getCurrentCharacter).filter(Objects::nonNull)
                            .toList());
                    if (game.getCurrentPlayer().getOrder() != 7) {
                        unavailableCharacters.add(game.getDownwardDiscard());
                    }
                    p.setUnavailableCharacters(unavailableCharacters);
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                        new GameUpdateDto(code, GameUpdateTypeEnum.PLAYER_TURN, unavailableCharacters));
                } else {
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                        new GameUpdateDto(code, GameUpdateTypeEnum.NEXT_PLAYER, game.getCurrentPlayer().getId()));
                }
            });
        } else {
            broadcastOnWebsocket(code, game.getPlayers(), GameUpdateTypeEnum.CHARACTER_REVEAL,
                playerMapper.entityToPublicResponse(game.getCurrentPlayer()));
        }

        gameRepository.save(game);
        return skippedCharacters;
    }

    public List<DistrictResponse> gatherResources(String code, ResourceTypeEnum resource, UserEntity loggedInUser) {
        log.info("Selecting resource {} for {} in game {}", resource, loggedInUser.getName(), code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.RESOURCE);
        logService.logResourceGathering(game, resource);

        if (resource.equals(ResourceTypeEnum.GOLD)) {
            game.getCurrentPlayer().giveGold(RESOURCE_GOLD);
            game.setPhase(GamePhaseEnum.TURN);
            gameRepository.save(game);
            broadcastOnWebsocket(code, game.getPlayers(), GameUpdateTypeEnum.RESOURCE_COLLECTION,
                new ResourceGatherResponse(resource, RESOURCE_GOLD));
        } else if (resource.equals(ResourceTypeEnum.CARDS)) {
            game.getCurrentPlayer().setDrawnCards(drawFromDeck(game, 2));
            gameRepository.save(game);
            return game.getCurrentPlayer().getDrawnCards().stream().map(cardMapper::entityToResponse).toList();
        }
        return Collections.emptyList();
    }

    public List<DistrictResponse> drawCards(String code, List<Integer> districtIndexes, UserEntity loggedInUser) {
        log.info("User {} choosing districts {} in game {}", loggedInUser.getName(), districtIndexes, code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.RESOURCE);

        List<DistrictEntity> drawnCards = new ArrayList<>();
        districtIndexes.forEach(index -> drawnCards.add(game.getCurrentPlayer().getDrawnCards().get(index)));
        game.getCurrentPlayer().giveCards(drawnCards);
        game.getCurrentPlayer().getDrawnCards().clear();
        game.setPhase(GamePhaseEnum.TURN);
        gameRepository.save(game);
        broadcastOnWebsocket(code, game.getPlayers(), GameUpdateTypeEnum.RESOURCE_COLLECTION,
            new ResourceGatherResponse(ResourceTypeEnum.CARDS, drawnCards.size()));
        return drawnCards.stream().map(cardMapper::entityToResponse).toList();
    }

    public void buildDistrict(String code, DistrictBuildRequest districtIndex, UserEntity loggedInUser) {
        log.info("User {} building district {} in game {}", loggedInUser.getName(), districtIndex, code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.TURN);
    }

    public void endTurn(String code, UserEntity loggedInUser) {
        log.info("User {} ending turn in game {}", loggedInUser.getName(), code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.TURN);
        setNextPlayer(game);
        broadcastOnWebsocket(code, game.getPlayers(), GameUpdateTypeEnum.NEXT_PLAYER,
            playerMapper.entityToPublicResponse(game.getCurrentPlayer()));
        gameRepository.save(game);
    }

    // Broadcasts the update to all the players of the game
    private void broadcastOnWebsocket(String code, List<PlayerEntity> players, GameUpdateTypeEnum updateType,
        Object change
    ) {
        players.forEach(m -> {
            log.info("Broadcasting {} to {}", updateType.getValue(), m.getUserId());
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(m.getUserId()), "/topic/game/update",
                new GameUpdateDto(code, updateType, change));
        });
    }

    // Starts the next selection phase of the game
    private void startSelectionPhase(GameEntity game) {
        Random r = new Random();
        int gameSize = game.getPlayers().size();
        Map<Integer, Integer> orderMap = assembleOrderMap(game.getCrownedPlayer().getOrder(), gameSize);

        game.setTurn(1);
        game.setPhase(GamePhaseEnum.SELECTION);
        game.setCurrentPlayer(game.getCrownedPlayer());
        game.setDownwardDiscard(r.nextInt(game.getCharacters().size()));
        game.setUpwardDiscard(discardCharacters(configRepository.findByTypeAndConfigId(
            game.getCharacters().size() == 9 ? ConfigTypeEnum.UPWARDS_CARDS_9C : ConfigTypeEnum.UPWARDS_CARDS_8C,
            (long) gameSize).getConfigValue(), gameSize, List.of(game.getDownwardDiscard(), 4)));
        game.setPlayers(game.getPlayers().stream().peek(p -> {
            p.setOrder(orderMap.get(p.getOrder()));
            if (p.getId().equals(game.getCurrentPlayer().getId())) {
                p.setUnavailableCharacters(new ArrayList<>(Collections.singletonList(game.getDownwardDiscard())));
            }
        }).collect(Collectors.toList()));
    }

    // Returns the game or throws an exception if it doesn't exist
    private GameEntity findGame(String code) {
        return gameRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));
    }

    // Removes n cards from the deck and returns them
    private List<DistrictEntity> drawFromDeck(GameEntity game, int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(game.getDeck().remove(0));
        }
        return drawnCards;
    }

    // Assembles the starting deck with the given unique districts
    private List<DistrictEntity> assembleDeck(List<Long> uniqueDistricts) {
        Map<Long, DistrictEntity> districts =
            districtRepository.findAll().stream().collect(Collectors.toMap(DistrictEntity::getId, d -> d));
        return shuffleDeck(
            Stream.concat(assembleBaseDeck().stream(), uniqueDistricts.stream()).map(districts::get).toList());
    }

    // Shuffles the deck
    private List<DistrictEntity> shuffleDeck(List<DistrictEntity> cards) {
        List<DistrictEntity> deck = new ArrayList<>(cards);
        Collections.shuffle(deck);
        return deck;
    }

    // Assembles the starting deck without unique districts
    private List<Long> assembleBaseDeck() {
        List<Long> baseDeck = new ArrayList<>();
        configRepository.findAllByType(ConfigTypeEnum.BASE_CARD).forEach(configEntity -> {
            for (int i = 0; i < configEntity.getConfigValue(); ++i) {
                baseDeck.add(configEntity.getConfigId());
            }
        });
        return baseDeck;
    }

    // Discards random characters depending on game size
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

    // Returns the IDs of the districts recommended for the first game
    private List<Long> getDefaultUniqueDistricts() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT).stream()
            .map(ConfigEntity::getConfigId).toList();
    }

    // Returns the IDs of the characters recommended for the first game
    private List<CharacterEntity> getDefaultCharacters(boolean hasEightMaxPlayers) {
        return characterRepository.findAllByIdIn(
            configRepository.findAllByType(ConfigTypeEnum.DEFAULT_CHARACTER).stream().filter(c -> {
                if (!hasEightMaxPlayers) {
                    return c.getConfigValue() != 9;
                }
                return true;
            }).map(ConfigEntity::getConfigId).toList());
    }

    // Ends the current player's turn and starts the next one's
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
                            .orElseThrow(() -> new CorruptedGameException(game.getLobby().getCode()));

                    nextPlayer.setIsRevealed(true);
                    game.setCurrentPlayer(nextPlayer);
                    game.setPhase(GamePhaseEnum.RESOURCE);
                }
            }
            case TURN -> {
                List<PlayerEntity> playersLeft = game.getPlayers().stream()
                    .filter(player -> player.getCurrentCharacter() > game.getCurrentPlayer().getCurrentCharacter())
                    .sorted(Comparator.comparingInt(PlayerEntity::getCurrentCharacter)).toList();
                if (playersLeft.isEmpty()) {
                    game.getPlayers().forEach(player -> {
                        player.setIsRevealed(false);
                        player.setCurrentCharacter(null);
                    });
                    game.setCurrentPlayer(game.getCrownedPlayer());
                    game.setPhase(GamePhaseEnum.SELECTION);
                } else {
                    game.setCurrentPlayer(playersLeft.get(0));
                    game.getCurrentPlayer().setIsRevealed(true);
                }
            }
            default -> log.warn("Unhandled phase {}", game.getPhase());
        }
    }

    // Validates if the game is in the given phase and the given player is on turn
    private void validateGameTurn(GameEntity game, Long userId, GamePhaseEnum phase) {
        if (!Objects.equals(game.getPhase(), phase)) {
            throw new InvalidPhaseActionException(game.getLobby().getCode(), phase);
        }
        if (!Objects.equals(game.getCurrentPlayer().getUserId(), userId)) {
            throw new NotOnTurnException(game.getLobby().getCode(), userId);
        }
    }

    // Returns a map that help reorder the players in case the crowned player changes
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
