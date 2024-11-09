package olter.loaf.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.lobbies.model.LobbyEntity;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String name;

    private String password;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<LobbyEntity> lobbies;
}
