package olter.loaf.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;

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

  private String displayName;
  private String password;
}
