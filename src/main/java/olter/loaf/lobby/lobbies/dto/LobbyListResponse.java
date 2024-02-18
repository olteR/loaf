package olter.loaf.lobby.lobbies.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.lobby.lobbies.model.LobbyStatusEnum;
import olter.loaf.users.dto.UserResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class LobbyListResponse {
    private String name;
    private String code;
    private Long owner;
    private Boolean secured;
    private Integer maxMembers;
    private LobbyStatusEnum status;
    private List<UserResponse> members;
}
