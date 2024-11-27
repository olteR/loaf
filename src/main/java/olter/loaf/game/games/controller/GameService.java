package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.cards.CardMapper;
import olter.loaf.game.cards.dto.AbilityRequest;
import olter.loaf.game.cards.dto.AbilityTargetRequest;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.*;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
import olter.loaf.game.games.GameMapper;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameUpdateDto;
import olter.loaf.game.games.dto.GameUpdateTypeEnum;
import olter.loaf.game.games.dto.ResourceGatherResponse;
import olter.loaf.game.games.exception.*;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.game.players.PlayerMapper;
import olter.loaf.game.players.model.ConditionEnum;
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
    private final PlayerMapper playerMapper;
    private final CardMapper cardMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Value("${loaf.config.starting-gold}")
    private Integer STARTING_GOLD;
    @Value("${loaf.config.starting-cards}")
    private Integer STARTING_CARDS;
    @Value("${loaf.config.resource-gold}")
    private Integer RESOURCE_GOLD;
    @Value("${loaf.config.resource-cards}")
    private Integer RESOURCE_CARDS;

    public GameEntity createGameForLobby(LobbyEntity lobby) {
        log.info("Creating game for lobby {}", lobby.getCode());
        GameEntity game = new GameEntity();
        game.setPhase(GamePhaseEnum.NOT_STARTED);
        game.setIsFinalTurn(false);
        game.setUniqueDistricts(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters(lobby.getMaxMembers().equals(8)));
        game.setTurn(0);
        gameRepository.save(game);

        PlayerEntity p = new PlayerEntity();
        p.setUserId(lobby.getOwner());
        p.setOrder(1);
        p.setGame(game);
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
            p.setHand(game.drawFromDeck(STARTING_CARDS));
            p.setPoints(0);
        }).collect(Collectors.toList()));

        game.newTurn();
        gameRepository.save(game);
    }

    public GameDetailsResponse getGame(String code, UserEntity loggedInUser) {
        log.info("Getting game details of {} for {}", code, loggedInUser.getName());
        GameEntity game = findGame(code);

        PlayerEntity player = playerRepository.findByUserIdAndGame(loggedInUser.getId(), game)
            .orElseThrow(() -> new NotInGameException(code, loggedInUser.getId()));
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

        game.getCurrentPlayer().setCharacter(selectedCharacter);
        game.getCurrentPlayer().setSkippedCharacters(skippedCharacters);
        logService.logCharacterSelection(game);

        game.nextPlayer();
        if (game.getPhase().equals(GamePhaseEnum.SELECTION)) {
            game.getPlayers().forEach(p -> {
                log.info("Broadcasting next player to {}", p.getUserId());
                if (game.getCurrentPlayer().getId().equals(p.getId())) {
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                        new GameUpdateDto(code, GameUpdateTypeEnum.PLAYER_TURN, p.getUnavailableCharacters()));
                } else {
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                        new GameUpdateDto(code, GameUpdateTypeEnum.NEXT_PLAYER, game.getCurrentPlayer().getId()));
                }
            });
            gameRepository.save(game);
        } else {
            gameRepository.save(game);
            broadcastOnWebsocket(code, game, GameUpdateTypeEnum.CHARACTER_REVEAL);
        }

        return skippedCharacters;
    }

    public List<DistrictResponse> gatherResources(String code, ResourceTypeEnum resource, UserEntity loggedInUser) {
        log.info("Selecting resource {} for {} in game {}", resource, loggedInUser.getName(), code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.RESOURCE);
        logService.logResourceGathering(game, resource);
        List<DistrictEntity> drawnCards = new ArrayList<>();

        if (resource.equals(ResourceTypeEnum.GOLD)) {
            Integer amount =
                game.getCurrentPlayer().hasDistrictAbility(AbilityEnum.GOLD_MINE) ? RESOURCE_GOLD + 1 : RESOURCE_GOLD;
            game.getCurrentPlayer().giveGold(amount);
            game.setPhase(GamePhaseEnum.TURN);
            handleWitchAbility(game);
            handleBlackmailerAbility(game);
            broadcastOnWebsocket(code, game, GameUpdateTypeEnum.RESOURCE_COLLECTION,
                new ResourceGatherResponse(resource, amount));
        } else if (resource.equals(ResourceTypeEnum.CARDS)) {
            drawnCards = game.drawFromDeck(RESOURCE_CARDS);
            if (game.getCurrentPlayer().hasDistrictAbility(AbilityEnum.OBSERVATORY)) {
                drawnCards.add(game.drawFromDeck(1).get(0));
            }
            if (game.getCurrentPlayer().hasDistrictAbility(AbilityEnum.LIBRARY) &&
                drawnCards.size() == RESOURCE_CARDS) {
                game.getCurrentPlayer().giveCards(drawnCards);
                game.setPhase(GamePhaseEnum.TURN);
                broadcastOnWebsocket(code, game, GameUpdateTypeEnum.RESOURCE_COLLECTION,
                    new ResourceGatherResponse(resource, RESOURCE_CARDS));
            } else {
                game.getCurrentPlayer().setDrawnCards(drawnCards);
            }
        }

        gameRepository.save(game);
        return drawnCards.stream().map(cardMapper::entityToResponse).toList();
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
        handleWitchAbility(game);
        handleBlackmailerAbility(game);

        gameRepository.save(game);
        broadcastOnWebsocket(code, game, GameUpdateTypeEnum.RESOURCE_COLLECTION,
            new ResourceGatherResponse(ResourceTypeEnum.CARDS, drawnCards.size()));
        return drawnCards.stream().map(cardMapper::entityToResponse).toList();
    }

    public void buildDistrict(String code, Integer handIndex, UserEntity loggedInUser) {
        log.info("User {} building district {} in game {}", loggedInUser.getName(), handIndex, code);
        GameEntity game = findGame(code);
        validateBuild(game, loggedInUser.getId(), handIndex);
        PlayerEntity player = game.getCurrentPlayer();
        DistrictEntity district = player.takeCard(handIndex);
        logService.logDistrictBuilding(game, district.getId());
        PlayerEntity magistrate = game.getPlayer(1);

        if (player.hasCondition(ConditionEnum.WARRANTED) && !magistrate.getDistricts().contains(district)) {
            magistrate.giveDistrict(district);
            player.removeCondition(ConditionEnum.WARRANTED);
            player.giveCondition(ConditionEnum.WARRANTED_SIGNED);
        } else {
            player.giveDistrict(district);
            player.takeGold(district.getCost());
        }
        if (!(player.hasCondition(ConditionEnum.BLOOMING_TRADE) && district.getType() == DistrictTypeEnum.TRADE) &&
            !district.hasAbility(AbilityEnum.STABLES)) {
            player.setBuildLimit(player.getBuildLimit() - 1);
        }
        if (player.hasCondition(ConditionEnum.BLOOMING_TRADE)) {
            player.setAbilityTarget(Long.valueOf(district.getCost()));
        }
        playerRepository.save(player);
        broadcastOnWebsocket(code, game, GameUpdateTypeEnum.BUILD);
    }

    public void useAbility(AbilityRequest request, UserEntity user) throws IllegalAccessException {
        log.info("User {} using ability {} in game {}", user.getName(), request.getAbility().getValue(),
            request.getCode());
        GameEntity game = findGame(request.getCode());
        validateAbilityUse(game, user.getId(), request.getAbility());

        request.getAbility().useAbility(game, request.getTarget());
        game.getCurrentPlayer().getUsedAbilities().add(request.getAbility());
        logService.logAbilityUse(game, request.getTarget(), request.getAbility());
        gameRepository.save(game);
        broadcastOnWebsocket(request.getCode(), game, GameUpdateTypeEnum.USE_ABILITY);
    }

    public void endTurn(String code, UserEntity loggedInUser) {
        log.info("User {} ending turn in game {}", loggedInUser.getName(), code);
        GameEntity game = findGame(code);
        validateGameTurn(game, loggedInUser.getId(), GamePhaseEnum.TURN);
        if (game.getCurrentPlayer().getCharacter().hasAbility(AbilityEnum.WITCH)) {
            game.setCurrentPlayer(game.getBewitchedPlayer());
        }
        game.nextPlayer();
        gameRepository.save(game);
        switch (game.getPhase()) {
            case SELECTION -> broadcastOnWebsocket(code, game, GameUpdateTypeEnum.NEW_TURN);
            case ENDED -> broadcastOnWebsocket(code, game, GameUpdateTypeEnum.END_GAME);
            default -> broadcastOnWebsocket(code, game, GameUpdateTypeEnum.CHARACTER_REVEAL);
        }
    }

    // Broadcasts the game details to all the players of the game
    private void broadcastOnWebsocket(String code, GameEntity game, GameUpdateTypeEnum updateType) {
        game.getPlayers().forEach(p -> {
            log.info("Broadcasting game details after {} to {}", updateType.getValue(), p.getUserId());
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                new GameUpdateDto(code, updateType, gameMapper.entityToDetailsResponse(game, p, code)));
        });
    }

    // Broadcasts the given change to all the players of the game
    private void broadcastOnWebsocket(String code, GameEntity game, GameUpdateTypeEnum updateType, Object change) {
        game.getPlayers().forEach(p -> {
            log.info("Broadcasting {} to {}", updateType.getValue(), p.getUserId());
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(p.getUserId()), "/topic/game/update",
                new GameUpdateDto(code, updateType, change));
        });
    }

    // Returns the game or throws an exception if it doesn't exist
    private GameEntity findGame(String code) {
        return gameRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(GameEntity.class.getName(), code));
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

    // Handles abilities related to Witch
    private void handleWitchAbility(GameEntity game) {
        if (game.getCurrentPlayer().getCharacter().hasAbility(AbilityEnum.WITCH)) {
            game.getCurrentPlayer().setUsingAbility(AbilityEnum.WITCH);
        }
        if (game.getCurrentPlayer().hasCondition(ConditionEnum.BEWITCHED)) {
            game.setCurrentPlayer(game.getPlayer(1));
            game.getPlayer(game.getBewitchedCharacter()).getCharacter().getAbilities().stream().filter(
                    ability -> ability.getType() == ActivationEnum.START_OF_TURN && ability != AbilityEnum.TAKE_CROWN)
                .forEach(ability -> ability.useAbility(game, null));
        }
    }

    // Handles abilities related to Witch
    private void handleBlackmailerAbility(GameEntity game) {
        if (game.getThreatenedCharacters().contains(game.getCurrentPlayer().getCharacterNumber())) {
            if (game.getCurrentPlayer().getGold() > 1) {
                game.getCurrentPlayer().setUsingAbility(AbilityEnum.PAY_OFF);
            }
            else {
                game.getCurrentPlayer().removeCondition(ConditionEnum.THREATENED);
            }
        }
    }

    // Validates if the game is in the given phase and the given player is on turn
    private void validateGameTurn(GameEntity game, Long userId, GamePhaseEnum phase) {
        if (!Objects.equals(game.getPhase(), phase)) {
            throw new InvalidPhaseActionException(game.getLobby().getCode());
        }
        if (!Objects.equals(game.getCurrentPlayer().getUserId(), userId)) {
            throw new NotOnTurnException(game.getLobby().getCode(), userId);
        }
    }

    // Validates if the district at the index is buildable
    private void validateBuild(GameEntity game, Long userId, Integer handIndex) {
        validateGameTurn(game, userId, GamePhaseEnum.TURN);
        PlayerEntity player = game.getCurrentPlayer();
        if (player.getHand().size() <= handIndex) {
            throw new InvalidDistricIndexException(player.getId(), handIndex);
        }
        if (player.getBuildLimit() == 0) {
            throw new BuildLimitException(player.getId());
        }
        DistrictEntity district = game.getCurrentPlayer().getHand().get(handIndex);
        if (district.getCost() > player.getGold()) {
            throw new NotEnoughGoldException(player.getId(), district.getId());
        }
        if (player.getDistricts().stream().map(DistrictEntity::getId).toList().contains(district.getId()) &&
            !player.hasCondition(ConditionEnum.DUPLICATES) && !player.hasDistrictAbility(AbilityEnum.QUARRY)) {
            throw new AlreadyBuiltException(player.getId(), district.getId());
        }
        if (district.hasAbility(AbilityEnum.SECRET_VAULT)) {
            throw new CannotBuildException(player.getId(), district.getId());
        }
    }

    // Validates if the given ability can be used by the player
    private void validateAbilityUse(GameEntity game, Long userId, AbilityEnum ability) {
        validateGameTurn(game, userId, GamePhaseEnum.TURN);
        if (game.getCurrentPlayer().getUsedAbilities().contains(ability) &&
            game.getCurrentPlayer().getUsingAbility() != ability) {
            throw new AlreadyUsedException(ability, userId);
        }
        switch (ability.getType()) {
            case MANUAL:
            case AFTER_GATHERING:
                if (game.getCurrentPlayer().getCharacter().hasAbility(AbilityEnum.WITCH)) {
                    if (game.getBewitchedPlayer() != null &&
                        !Stream.concat(game.getCurrentPlayer().getCharacter().getAbilities().stream(),
                                game.getCharacters().get(game.getBewitchedCharacter() - 1).getAbilities().stream()).toList()
                            .contains(ability)) {
                        throw new InvalidActivationException(game.getCurrentPlayer().getId(), ability);
                    }
                } else if (!game.getCurrentPlayer().getCharacter().hasAbility(ability)) {
                    throw new InvalidActivationException(game.getCurrentPlayer().getId(), ability);
                }
                break;
            case BEFORE_BUILD:
                if (!game.getCurrentPlayer().getHand().stream().flatMap(district -> district.getAbilities().stream())
                    .toList().contains(ability)) {
                    throw new InvalidActivationException(game.getCurrentPlayer().getId(), ability);
                }
                break;
            case AFTER_BUILD:
                if (!game.getCurrentPlayer().getDistricts().stream()
                    .flatMap(district -> district.getAbilities().stream()).toList().contains(ability)) {
                    throw new InvalidActivationException(game.getCurrentPlayer().getId(), ability);
                }
                break;
            default:
                throw new InvalidActivationException(game.getCurrentPlayer().getId(), ability);
        }
    }
}
