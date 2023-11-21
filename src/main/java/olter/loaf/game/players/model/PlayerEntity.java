package olter.loaf.game.players.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.games.model.GameEntity;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "players")
public class PlayerEntity extends BaseEntity {
    private Integer gold;
    private Long currentCharacter;

    @ElementCollection
    @CollectionTable(name = "player_hand", joinColumns = @JoinColumn(name = "player_id"))
    private List<Long> hand;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;
}
