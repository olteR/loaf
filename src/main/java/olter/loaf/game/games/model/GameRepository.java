package olter.loaf.game.games.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
