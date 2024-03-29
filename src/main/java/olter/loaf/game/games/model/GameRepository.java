package olter.loaf.game.games.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
