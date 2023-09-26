package olter.loaf.game.players.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.games.model.GameEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "players")
public class PlayerEntity extends BaseEntity {
    private Integer gold;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;
}
