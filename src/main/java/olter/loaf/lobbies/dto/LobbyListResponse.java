package olter.loaf.lobbies.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    private List<UserResponse> members;
}
