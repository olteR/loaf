package olter.loaf.game.games.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobbies.model.LobbyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    // Removes n cards from the deck and returns them
    public List<DistrictEntity> drawFromDeck(int cardCount) {
        List<DistrictEntity> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            drawnCards.add(deck.remove(0));
        }
        return drawnCards;
    }

    public PlayerEntity getCrownedPlayer() {
        return players.stream().filter(player -> player.getConditions().contains(ConditionEnum.CROWNED)).findFirst()
            .orElseThrow(() -> new CorruptedGameException(lobby.getCode()));
    }

    public void setCrownedPlayer(PlayerEntity player) {
        players = players.stream().peek(p -> {
            if (p.getId().equals(player.getId())) {
                p.getConditions().add(ConditionEnum.CROWNED);
            } else {
                p.getConditions().remove(ConditionEnum.CROWNED);
            }
        }).toList();
    }

    public void setKilledCharacter(Integer character) {
        killedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.getConditions().add(ConditionEnum.KILLED));
    }

    public void setRobbedCharacter(Integer character) {
        robbedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.getConditions().add(ConditionEnum.ROBBED));
    }

    public void setBewitchedCharacter(Integer character) {
        bewitchedCharacter = character;
        getPlayerWithCharacter(character).ifPresent(player -> player.getConditions().add(ConditionEnum.BEWITCHED));
    }

    private Optional<PlayerEntity> getPlayerWithCharacter(Integer character) {
        return players.stream().filter(player -> Objects.equals(player.getCharacterNumber(), character)).findFirst();
    }
}
