package olter.loaf.lobbies.lobby.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyCreationRequest {
  private String name;
  private String password;
  private boolean hidden;
  private boolean secured;
  private int maxMembers;
}
