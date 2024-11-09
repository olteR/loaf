package olter.loaf.lobbies.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.lobbies.model.LobbyStatusEnum;

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
    private LobbyStatusEnum status;
    private List<LobbyMemberDto> members;
    private GameSettingsResponse gameSettings;
}
