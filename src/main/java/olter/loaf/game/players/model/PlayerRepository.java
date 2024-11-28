package olter.loaf.game.players.model;

import olter.loaf.game.games.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByUserIdAndGame(Long userId, GameEntity game);

    List<PlayerEntity> findAllByGameAndOrderGreaterThan(GameEntity game, Integer gt);

    @Query("SELECT p FROM PlayerEntity p WHERE p.game.phase = 'ENDED' AND p.userId = ?1")
    List<PlayerEntity> findAllEndedByUserId(Long userId);
}
