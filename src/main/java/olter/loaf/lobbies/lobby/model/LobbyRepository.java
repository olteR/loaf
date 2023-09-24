package olter.loaf.lobbies.lobby.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
  boolean existsByCode(String code);

  Optional<LobbyEntity> findFirstByCode(String code);
}
