package olter.loaf.lobbies.lobby.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.users.model.UserEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "lobbies")
public class LobbyEntity extends BaseEntity {
  @Column(unique = true)
  private String name;

  @Column(unique = true)
  private String code;

  private Boolean hidden;
  private Boolean secured;
  private Integer maxMembers;

  private Long owner;
  private String password;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "lobby_users",
      joinColumns = {@JoinColumn(name = "lobby_id")},
      inverseJoinColumns = {@JoinColumn(name = "user_id")})
  private List<UserEntity> members;
}
