package olter.loaf.game.games.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobbies.model.LobbyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "games")
public class GameEntity extends BaseEntity {
    private Integer turn;
    private Integer downwardDiscard;

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
    @JoinColumn(name = "crowned_player", referencedColumnName = "id")
    private PlayerEntity crownedPlayer;

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

    public Integer getKilledCharacter() {
        return getConditionedPlayer(ConditionEnum.KILLED).getCurrentCharacter();
    }

    public void setKilledCharacter(Integer character) {
        getPlayerWithCharacter(character).getConditions().add(ConditionEnum.KILLED);
    }

    public void setRobbedCharacter(Integer character) {
        getPlayerWithCharacter(character).getConditions().add(ConditionEnum.ROBBED);
    }

    public Integer getBewitchedCharacter() {
        return getConditionedPlayer(ConditionEnum.BEWITCHED).getCurrentCharacter();
    }

    public void setBewitchedCharacter(Integer character) {
        getPlayerWithCharacter(character).getConditions().add(ConditionEnum.BEWITCHED);
    }

    private PlayerEntity getConditionedPlayer(ConditionEnum condition) {
        return players.stream().filter(player -> player.getConditions().contains(condition)).findFirst().orElse(null);
    }

    private PlayerEntity getPlayerWithCharacter(Integer character) {
        return players.stream().filter(player -> Objects.equals(player.getCurrentCharacter(), character)).findFirst()
            .orElse(null);
    }
}
