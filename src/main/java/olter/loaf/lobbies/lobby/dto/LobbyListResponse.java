package olter.loaf.lobbies.lobby.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.users.dto.UserResponse;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyListResponse {
  private String name;
  private Long owner;
  private List<UserResponse> members;
}
