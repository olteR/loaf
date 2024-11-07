package olter.loaf.lobby.lobbies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LobbyMemberDto {
    private Long id;
    private String name;
    private String displayName;
    private Integer order;
}
