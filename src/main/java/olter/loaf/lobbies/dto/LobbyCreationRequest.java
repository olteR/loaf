package olter.loaf.lobbies.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyCreationRequest {
  private String name;
  private String password;
  private Boolean hidden;
  private Boolean secured;
  private Integer maxMembers;
}
