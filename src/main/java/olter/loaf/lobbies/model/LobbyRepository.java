package olter.loaf.lobbies.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LobbyRepository extends JpaRepository<LobbyEntity, Long> {
    boolean existsByCode(String code);

    Optional<LobbyEntity> findByCode(String code);
}
