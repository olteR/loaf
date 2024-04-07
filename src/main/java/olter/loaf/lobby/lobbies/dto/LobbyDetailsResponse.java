package olter.loaf.lobby.lobbies.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.dto.GameSettingsResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyDetailsResponse {
    private Long id;
    private String name;
    private String code;
    private Boolean secured;
    private Integer maxMembers;
    private Long owner;
    private List<LobbyMemberResponse> members;
    private GameSettingsResponse gameSettings;
}
