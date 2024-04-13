package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PublicPlayerResponse {
    private Long userId;
    private Integer gold;
    private Integer handSize;
    private List<PlayerDistrictResponse> districts;
    private Integer currentCharacter;
}
