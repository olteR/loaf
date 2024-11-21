package olter.loaf.game.games.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.model.ActivationEnum;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.exception.InvalidPhaseActionException;
import olter.loaf.game.players.model.ConditionDurationEnum;
import olter.loaf.game.players.model.ConditionEnum;
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

    @Enumerated(EnumType.STRING)
    private GamePhaseEnum phase;

    @ElementCollection
    @CollectionTable(name = "game_discarded_characters", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "character_id")
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
        return players.stream().filter(player -> player.getId().equals(id)).findFirst()
            .orElse(null);
    }

    public PlayerEntity getPlayer(Integer character) {
        return players.stream().filter(player -> player.getCharacterNumber().equals(character)).findFirst()
            .orElse(null);
    }

    public PlayerEntity getCrownedPlayer() {
        return players.stream().filter(player -> player.hasCondition(ConditionEnum.CROWNED)).findFirst().orElse(null);
    }

    public void setCrownedPlayer(PlayerEntity player) {
        players = players.stream().peek(p -> {
            if (p.getId().equals(player.getId())) {
                p.giveCondition(ConditionEnum.CROWNED);
            } else {
                p.removeCondition(ConditionEnum.CROWNED);
            }
        }).collect(Collectors.toList());
    }

    public void setKilledCharacter(Integer character) {
        killedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.KILLED));
    }

    public void setRobbedCharacter(Integer character) {
        robbedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.ROBBED));
    }

    public void setBewitchedCharacter(Integer character) {
        bewitchedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.giveCondition(ConditionEnum.BEWITCHED));
    }

    private Optional<PlayerEntity> getPlayerWithCharacter(Integer character) {
        return players.stream().filter(player -> Objects.equals(player.getCharacterNumber(), character)).findFirst();
    }

    // Removes n cards from the deck and returns them
    public List<DistrictEntity> drawFromDeck(int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(deck.remove(0));
        }
        return drawnCards;
    }

    // Starts a new turn
    public void newTurn() {
        Random r = new Random();
        Map<Integer, Integer> orderMap = assembleOrderMap(getCrownedPlayer().getOrder(), players.size());

        turn++;
        phase = GamePhaseEnum.SELECTION;
        currentPlayer = getCrownedPlayer();
        killedCharacter = null;
        bewitchedCharacter = null;
        robbedCharacter = null;
        downwardDiscard = r.nextInt(characters.size() + 1);
        upwardDiscard = discardCharacters(List.of(downwardDiscard, 4));
        players = players.stream().peek(p -> {
            p.setCharacter(null);
            p.setRevealed(false);
            p.setBuildLimit(1);
            p.setOrder(orderMap.get(p.getOrder()));
            if (p.getId().equals(currentPlayer.getId())) {
                p.setUnavailableCharacters(new ArrayList<>(Collections.singletonList(downwardDiscard)));
            }
            p.setConditions(p.getConditions().stream()
                .filter(condition -> condition.getDuration() != ConditionDurationEnum.END_OF_TURN)
                .collect(Collectors.toList()));
            p.setUsedAbilities(new ArrayList<>());
        }).collect(Collectors.toList());
    }

    // Ends the current player's turn and starts the next one's
    public void nextPlayer() {
        switch (phase) {
            case SELECTION -> {
                if (currentPlayer.getOrder() < players.size()) {
                    currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
                    List<Integer> unavailableCharacters =
                        players.stream().map(PlayerEntity::getCharacterNumber).filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    if (currentPlayer.getOrder() != 7) {
                        unavailableCharacters.add(downwardDiscard);
                    }
                    currentPlayer.setUnavailableCharacters(unavailableCharacters);
                } else {
                    Integer firstChar =
                        players.stream().map(PlayerEntity::getCharacterNumber).min(Integer::compareTo).get();
                    PlayerEntity nextPlayer =
                        players.stream().filter(p -> p.getCharacterNumber().equals(firstChar)).findFirst()
                            .orElseThrow(() -> new CorruptedGameException(lobby.getCode()));
                    revealPlayer(nextPlayer);
                }
            }
            case TURN -> {
                List<PlayerEntity> playersLeft =
                    players.stream().filter(player -> player.getCharacterNumber() > currentPlayer.getCharacterNumber())
                        .sorted(Comparator.comparingInt(PlayerEntity::getCharacterNumber)).toList();
                if (playersLeft.isEmpty()) {
                    if (Objects.equals(killedCharacter, 4) && getPlayer(4) != null) {
                        setCrownedPlayer(getPlayer(4));
                    }
                    newTurn();
                } else {
                    revealPlayer(playersLeft.get(0));
                }
            }
            default -> throw new InvalidPhaseActionException(lobby.getCode());
        }
    }

    // Reveals player's character and starts their turn
    private void revealPlayer(PlayerEntity player) {
        currentPlayer = player;
        currentPlayer.setRevealed(true);
        if (player.hasCondition(ConditionEnum.KILLED)) {
            nextPlayer();
        } else if (player.hasCondition(ConditionEnum.ROBBED)) {
            getPlayer(2).giveGold(currentPlayer.takeGold(currentPlayer.getGold()));
        }
        phase = GamePhaseEnum.RESOURCE;
        currentPlayer.getCharacter().getAbilities().forEach(ability -> {
            if (ability.getType() == ActivationEnum.START_OF_TURN) {
                ability.useAbility(this, null);
            }
        });
    }

    // Discards random characters depending on game size
    private List<Integer> discardCharacters(List<Integer> excludes) {
        int discardCount = Math.max(6 - players.size(), 0);
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
}
