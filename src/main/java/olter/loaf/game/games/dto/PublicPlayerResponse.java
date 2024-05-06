package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PublicPlayerResponse {
    private Long id;
    private Long userId;
    private String name;
    private String displayName;
    private Integer gold;
    private Integer handSize;
    private List<PlayerDistrictResponse> districts;
    private Integer currentCharacter;
}
