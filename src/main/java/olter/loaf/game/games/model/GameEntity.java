package olter.loaf.game.games.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobbies.model.LobbyEntity;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "games")
public class GameEntity extends BaseEntity {
    private Long crownedPlayer;
    private Long currentPlayer;
    private Integer turn;

    @Enumerated(EnumType.STRING)
    private GamePhaseEnum phase;

    @ElementCollection
    @CollectionTable(name = "game_characters", joinColumns = @JoinColumn(name = "game_id"))
    private List<Long> characters;

    @ElementCollection
    @CollectionTable(name = "game_unique_districts", joinColumns = @JoinColumn(name = "game_id"))
    private List<Long> uniqueDistricts;

    @ElementCollection
    @CollectionTable(name = "game_deck", joinColumns = @JoinColumn(name = "game_id"))
    private List<Long> deck;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
    private LobbyEntity lobby;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
    private List<PlayerEntity> players;
}
