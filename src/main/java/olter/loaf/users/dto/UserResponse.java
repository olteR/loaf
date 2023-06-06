package olter.loaf.users.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserResponse {
  private Integer id;
  private String name;
  private String displayName;
}
