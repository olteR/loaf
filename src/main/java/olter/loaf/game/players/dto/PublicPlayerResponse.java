package olter.loaf.game.players.dto;

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
    private Integer gold;
    private Integer handSize;
    private List<PlayerDistrictResponse> districts;
    private Integer currentCharacter;
}
