package olter.loaf.lobbies.lobby.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyCreationRequest {
    private String name;
    private String password;
    private Boolean hidden;
    private Boolean secured;
    private Integer maxMembers;
}
