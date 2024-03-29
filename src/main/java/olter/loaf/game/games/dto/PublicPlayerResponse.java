package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PublicPlayerResponse {
    private String displayName;
    private String name;
    private Integer gold;
    private List<PlayerDistrictResponse> districts;
    private Long currentCharacter;
}
