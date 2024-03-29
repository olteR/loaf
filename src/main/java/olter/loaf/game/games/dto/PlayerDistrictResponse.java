package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerDistrictResponse {
    private Long districtId;
    private Boolean isBeautified;
}
