package olter.loaf.lobbies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyRequest {
    private String name;
    private String password;
    private Boolean hidden;
    private Boolean secured;
    private Integer maxMembers;
}
