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
        this.downwardDiscard = r.nextInt(this.characters.size() + 1);
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
        this.currentPlayer = player;
        this.currentPlayer.setRevealed(true);
        if (player.hasCondition(ConditionEnum.KILLED)) {
            nextPlayer();
        } else if (player.hasCondition(ConditionEnum.ROBBED)) {
            getPlayer(2).giveGold(this.currentPlayer.takeGold(this.currentPlayer.getGold()));
        }
        this.phase = GamePhaseEnum.RESOURCE;
        this.currentPlayer.getCharacter().getAbilities().forEach(ability -> {
            if (ability.getType() == ActivationEnum.START_OF_TURN) {
                ability.useAbility(this, null);
            }
        });
    }

    // Discards random characters depending on game size
    private List<Integer> discardCharacters(List<Integer> excludes) {
        int discardCount = Math.max(6 - this.players.size(), 0);
        Random r = new Random();
        List<Integer> discardedCharacters = new ArrayList<>();
        while (discardedCharacters.size() < discardCount) {
            Integer chosenCharacter = r.nextInt(8) + 1;
            if (!excludes.contains(chosenCharacter)) {
                discardedCharacters.add(chosenCharacter);
            }
        }
        return discardedCharacters;
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
        players.forEach(player -> {
            List<DistrictTypeEnum> missingTypes =
                Arrays.asList(DistrictTypeEnum.NOBLE, DistrictTypeEnum.RELIGIOUS, DistrictTypeEnum.TRADE,
                    DistrictTypeEnum.MILITARY, DistrictTypeEnum.UNIQUE);
            player.getDistricts().forEach(district -> {
                missingTypes.remove(district.getType());
                district.getAbilities().forEach(ability -> {
                    if (ability.getType() == ActivationEnum.END_OF_GAME) {
                        AbilityTargetRequest target = new AbilityTargetRequest();
                        target.setId(player.getId());
                        ability.useAbility(this, target);
                    }
                });
                if (missingTypes.isEmpty()) {
                    player.givePoints(3);
                }
            });
            if (player.hasDistrictAbility(AbilityEnum.SECRET_VAULT)) {
                player.givePoints(3);
            }
        });
    }
}
