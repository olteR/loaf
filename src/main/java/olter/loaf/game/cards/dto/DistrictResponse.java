package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.DistrictTypeEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class DistrictResponse {
    private Long id;
    private String name;
    private String cardName;
    private DistrictTypeEnum type;
    private Integer cost;
    private List<AbilityResponse> abilities;
}
