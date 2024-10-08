package olter.loaf.users.model;

import olter.loaf.lobby.lobbies.dto.LobbyMemberResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);

    @Query(
        "SELECT new olter.loaf.lobby.lobbies.dto.LobbyMemberResponse(u.id, u.displayName, u.name, p.order) FROM " +
            "UserEntity u JOIN u.lobbies l JOIN PlayerEntity p ON u.id = p.userId AND p.game.id = l.game.id WHERE l" +
            ".code = ?1 ORDER BY p.order ASC")
    List<LobbyMemberResponse> getLobbyMembers(String code);
}
