package olter.loaf.lobbies.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LobbyMemberInteractionDto {
    private String code;
    private Long memberId;
}
