package olter.loaf.lobbies.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyJoinRequest {
    private String code;
    private String password;
}
