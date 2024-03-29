package olter.loaf.game.players.model;

import olter.loaf.game.games.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findFirstByUserIdAndGame(Long userId, GameEntity game);
}
