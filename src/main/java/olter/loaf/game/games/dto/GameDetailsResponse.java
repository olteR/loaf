package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.users.dto.UserResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameDetailsResponse {
    private Long gameId;
    private List<Long> characters;
    private List<UserResponse> members;
}
