package olter.loaf.game.logs.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findAllByPlayerIdIn(List<Long> playerIds);
}
