package olter.loaf.game.cards.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {
    List<CharacterEntity> findAllByIdIn(List<Long> ids);
}
