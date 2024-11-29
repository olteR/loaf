package olter.loaf.game.logs.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
    @Query("SELECT l FROM LogEntity l LEFT JOIN PlayerEntity p ON p.id = l.playerId WHERE l.playerId IN ?1 AND p.game.phase = 'ENDED'")
    List<LogEntity> findAllByPlayerIdInAndGameEnded(List<Long> playerIds);
}
