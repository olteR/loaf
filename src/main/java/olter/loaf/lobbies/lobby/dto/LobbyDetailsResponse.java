package olter.loaf.lobbies.lobby.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.users.dto.UserResponse;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyDetailsResponse {
  private Long id;
  private String name;
  private String code;
  private Boolean hidden;
  private Boolean secured;
  private Integer maxMembers;
  private Long owner;
  private List<UserResponse> members;
}
