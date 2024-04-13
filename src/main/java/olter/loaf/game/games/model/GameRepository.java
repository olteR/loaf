package olter.loaf.game.games.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Query("SELECT g FROM GameEntity g JOIN g.lobby l WHERE l.code = ?1")
    Optional<GameEntity> findByCode(String code);
}
