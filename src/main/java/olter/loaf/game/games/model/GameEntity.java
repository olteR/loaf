package olter.loaf.game.games.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.dto.AbilityTargetRequest;
import olter.loaf.game.cards.model.*;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.exception.InvalidPhaseActionException;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.DurationEnum;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobbies.model.LobbyEntity;
import olter.loaf.lobbies.model.LobbyStatusEnum;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "games")
public class GameEntity extends BaseEntity {
    private Integer turn;
    private Integer downwardDiscard;
    private Integer killedCharacter;
    private Integer robbedCharacter;
    private Integer bewitchedCharacter;
    private Boolean isFinalTurn;

    @Enumerated(EnumType.STRING)
    private GamePhaseEnum phase;

    @ElementCollection
    @CollectionTable(name = "game_warranted_characters", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "character_number")
    private List<Integer> warrantedCharacters;

    @ElementCollection
    @CollectionTable(name = "game_threatened_characters", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "character_number")
    private List<Integer> threatenedCharacters;

    @ElementCollection
    @CollectionTable(name = "game_discarded_characters", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "character_number")
    private List<Integer> upwardDiscard;

    @ElementCollection
    @CollectionTable(name = "game_unique_districts", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "district_id")
    private List<Long> uniqueDistricts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "current_player", referencedColumnName = "id")
    private PlayerEntity currentPlayer;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "game")
    private LobbyEntity lobby;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
    @OrderBy("order ASC")
    private List<PlayerEntity> players = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_characters", joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "character_id"))
    @OrderBy("number ASC")
    private List<CharacterEntity> characters;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_deck", joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> deck;

    public PlayerEntity getPlayer(Long id) {
        return this.players.stream().filter(player -> player.getId().equals(id)).findFirst().orElse(null);
    }

    public PlayerEntity getPlayer(Integer character) {
        return this.players.stream().filter(player -> player.getCharacterNumber().equals(character)).findFirst()
            .orElse(null);
    }

    public PlayerEntity getCrownedPlayer() {
        return this.players.stream().filter(player -> player.hasCondition(ConditionEnum.CROWNED)).findFirst()
            .orElse(null);
    }

    public void setCrownedPlayer(PlayerEntity player) {
        this.players = this.players.stream().peek(p -> {
            if (p.getId().equals(player.getId())) {
                p.giveCondition(ConditionEnum.CROWNED);
            } else {
                p.removeCondition(ConditionEnum.CROWNED);
            }
        }).collect(Collectors.toList());
    }

    public void setKilledCharacter(Integer character) {
        this.killedCharacter = character;
        this.getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.KILLED));
    }

    public void setRobbedCharacter(Integer character) {
        this.robbedCharacter = character;
        this.getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.ROBBED));
    }

    public void setBewitchedCharacter(Integer character) {
        this.bewitchedCharacter = character;
        this.getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.BEWITCHED));
    }

    public PlayerEntity getBewitchedPlayer() {
        return this.getPlayerWithCharacter(this.bewitchedCharacter).orElse(null);
    }

    private Optional<PlayerEntity> getPlayerWithCharacter(Integer character) {
        return this.players.stream().filter(player -> Objects.equals(player.getCharacterNumber(), character))
            .findFirst();
    }

    // Removes n cards from the deck and returns them
    public List<DistrictEntity> drawFromDeck(int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount && !this.deck.isEmpty(); i++) {
            drawnCards.add(this.deck.remove(0));
        }
        return drawnCards;
    }

    // Starts a new turn
    public void newTurn() {
        Random r = new Random();
        Map<Integer, Integer> orderMap = assembleOrderMap(getCrownedPlayer().getOrder(), this.players.size());

        this.turn++;
        this.phase = GamePhaseEnum.SELECTION;
        this.currentPlayer = getCrownedPlayer();
        this.killedCharacter = null;
        this.bewitchedCharacter = null;
        this.robbedCharacter = null;
        this.warrantedCharacters = new ArrayList<>();
        this.threatenedCharacters = new ArrayList<>();
        this.downwardDiscard = r.nextInt(this.characters.size() - 1) + 1;
        this.upwardDiscard = discardCharacters(List.of(this.downwardDiscard, 4));
        this.players = this.players.stream().peek(p -> {
            p.setCharacter(null);
            p.setRevealed(false);
            p.setBuildLimit(1);
            p.setOrder(orderMap.get(p.getOrder()));
            if (p.getId().equals(this.currentPlayer.getId())) {
                p.setUnavailableCharacters(new ArrayList<>(Collections.singletonList(this.downwardDiscard)));
            }
            p.setConditions(
                p.getConditions().stream().filter(condition -> condition.getDuration() != DurationEnum.END_OF_TURN)
                    .collect(Collectors.toSet()));
            p.setUsedAbilities(new HashSet<>());
        }).collect(Collectors.toList());
    }

    // Ends the current player's turn and starts the next one's
    public void nextPlayer() {
        switch (this.phase) {
            case SELECTION -> {
                if (this.currentPlayer.getOrder() < this.players.size()) {
                    this.currentPlayer = this.players.get(this.players.indexOf(this.currentPlayer) + 1);
                    List<Integer> unavailableCharacters =
                        this.players.stream().map(PlayerEntity::getCharacterNumber).filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    if (this.currentPlayer.getOrder() != 7) {
                        unavailableCharacters.add(this.downwardDiscard);
                    }
                    this.currentPlayer.setUnavailableCharacters(unavailableCharacters);
                } else {
                    for (PlayerEntity player : this.players) {
                        if (player.hasDistrictAbility(AbilityEnum.THEATER)) {
                            Random r = new Random();
                            PlayerEntity otherPlayer =
                                this.players.stream().filter(p -> !p.getId().equals(player.getId())).toList()
                                    .get(r.nextInt(this.players.size() - 1));
                            Integer temp = player.getCharacterNumber();
                            player.setCharacter(otherPlayer.getCharacterNumber());
                            otherPlayer.setCharacter(temp);
                        }
                    }
                    Integer firstChar =
                        this.players.stream().map(PlayerEntity::getCharacterNumber).min(Integer::compareTo).get();
                    PlayerEntity nextPlayer =
                        this.players.stream().filter(p -> p.getCharacterNumber().equals(firstChar)).findFirst()
                            .orElseThrow(() -> new CorruptedGameException(this.lobby.getCode()));
                    this.revealPlayer(nextPlayer);
                }
            }
            case TURN -> {
                if (currentPlayer.hasDistrictAbility(AbilityEnum.PARK) && currentPlayer.getHand().isEmpty()) {
                    currentPlayer.giveCards(this.drawFromDeck(2));
                }
                if (currentPlayer.hasDistrictAbility(AbilityEnum.POOR_HOUSE) && currentPlayer.getGold() == 0) {
                    currentPlayer.giveGold(1);
                }
                if (currentPlayer.getCharacter().hasAbility(AbilityEnum.ALCHEMIST) &&
                    currentPlayer.getAbilityTarget() != null) {
                    currentPlayer.giveGold(currentPlayer.getAbilityTarget().intValue());
                    currentPlayer.setAbilityTarget(null);
                }
                List<PlayerEntity> playersLeft = this.players.stream()
                    .filter(player -> player.getCharacterNumber() > this.currentPlayer.getCharacterNumber())
                    .sorted(Comparator.comparingInt(PlayerEntity::getCharacterNumber)).toList();
                if (playersLeft.isEmpty()) {
                    if (Objects.equals(this.killedCharacter, 4) && getPlayer(4) != null) {
                        setCrownedPlayer(getPlayer(4));
                    }
                    if (this.isFinalTurn) {
                        this.endGame();
                    } else {
                        this.newTurn();
                    }
                } else {
                    this.revealPlayer(playersLeft.get(0));
                }
            }
            default -> throw new InvalidPhaseActionException(this.lobby.getCode());
        }
    }

    // Reveals player's character and starts their turn
    private void revealPlayer(PlayerEntity player) {
        Integer currentTurn = this.turn;
        this.currentPlayer = player;
        this.currentPlayer.setRevealed(true);
        if (player.hasCondition(ConditionEnum.KILLED)) {
            nextPlayer();
        } else if (player.hasCondition(ConditionEnum.ROBBED)) {
            getPlayer(2).giveGold(this.currentPlayer.takeGold(this.currentPlayer.getGold()));
        }
        if (currentTurn.equals(this.turn)) {
            this.phase = GamePhaseEnum.RESOURCE;
            this.currentPlayer.getCharacter().getAbilities().forEach(ability -> {
                if (ability.getType() == ActivationEnum.START_OF_TURN) {
                    ability.useAbility(this, null);
                }
            });
        }
    }

    // Discards random characters depending on game size
    private List<Integer> discardCharacters(List<Integer> excludes) {
        int discardCount = Math.max(6 - this.players.size(), 0);
        Random r = new Random();
        Set<Integer> discardedCharacters = new HashSet<>();
        while (discardedCharacters.size() < discardCount) {
            Integer chosenCharacter = r.nextInt(characters.size() - 1) + 1;
            if (!excludes.contains(chosenCharacter)) {
                discardedCharacters.add(chosenCharacter);
            }
        }
        return new ArrayList<>(discardedCharacters);
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

    private void endGame() {
        phase = GamePhaseEnum.ENDED;
        lobby.setStatus(LobbyStatusEnum.ENDED);
        players.forEach(player -> {
            Map<DistrictTypeEnum, Integer> districtCountMap = new HashMap<>();
            List<DistrictTypeEnum> districtTypes = new ArrayList<>(EnumSet.allOf(DistrictTypeEnum.class));
            districtTypes.forEach(type -> districtCountMap.put(type, 0));

            for (DistrictEntity district : player.getDistricts()) {
                districtCountMap.put(district.getType(), districtCountMap.get(district.getType()) + 1);
                district.getAbilities().forEach(ability -> {
                    if (ability.getType() == ActivationEnum.END_OF_GAME) {
                        AbilityTargetRequest target = new AbilityTargetRequest();
                        target.setId(player.getId());
                        ability.useAbility(this, target);
                    }
                });
            }

            DistrictTypeEnum capitolType = null;
            Boolean hasCapitol = player.hasDistrictAbility(AbilityEnum.CAPITOL);
            if (hasCapitol) {
                capitolType =
                    districtTypes.stream().filter(type -> districtCountMap.get(type) > 2).findFirst().orElse(null);
                if (capitolType != null) {
                    player.giveBonusPoints(3);
                }
            }

            if (player.hasDistrictAbility(AbilityEnum.HAUNTED_QUARTER)) {
                // Fill missing type for bonus if there is another unique district
                List<DistrictTypeEnum> missingTypes =
                    districtTypes.stream().filter(type -> districtCountMap.get(type) == 0).toList();
                List<DistrictTypeEnum> capitolTypes = districtTypes.stream()
                    .filter(type -> type != DistrictTypeEnum.UNIQUE && districtCountMap.get(type) == 2).toList();
                if (missingTypes.size() == 1 && districtCountMap.get(DistrictTypeEnum.UNIQUE) > 1) {
                    districtCountMap.put(DistrictTypeEnum.UNIQUE, districtCountMap.get(DistrictTypeEnum.UNIQUE) - 1);
                    districtCountMap.put(missingTypes.get(0), 1);
                    if (player.hasDistrictAbility(AbilityEnum.WISHING_WELL)) {
                        player.getResults().setBonusPoints(player.getResults().getBonusPoints() - 1);
                    }
                }
                // Fill third type if player has unused Capitol
                else if (player.hasDistrictAbility(AbilityEnum.CAPITOL) && capitolType == null &&
                    !capitolTypes.isEmpty()) {
                    districtCountMap.put(DistrictTypeEnum.UNIQUE, districtCountMap.get(DistrictTypeEnum.UNIQUE) - 1);
                    districtCountMap.put(capitolTypes.get(0), 3);
                    player.giveBonusPoints(player.hasDistrictAbility(AbilityEnum.WISHING_WELL) ? 2 : 3);
                }
            }

            if (districtTypes.stream().noneMatch(type -> districtCountMap.get(type) == 0)) {
                player.getResults().setHasAllTypes(true);
            }

            if (player.getHand().stream().flatMap(district -> district.getAbilities().stream()).toList()
                .contains(AbilityEnum.SECRET_VAULT)) {
                player.giveBonusPoints(3);
            }
        });

        players.sort(Comparator.comparing(PlayerEntity::getPoints));
        Collections.reverse(players);
        int n = 1;
        players.get(0).getResults().setPlacement(n);
        for (int i = 1; i < players.size(); i++) {
            if (Objects.equals(players.get(i - 1).getPoints(), players.get(i).getPoints())) {
                players.get(i).getResults().setPlacement(n);
            } else {
                players.get(i).getResults().setPlacement(++n);
            }
        }
    }
}
