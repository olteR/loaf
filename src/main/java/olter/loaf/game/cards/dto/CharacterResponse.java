package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.DistrictTypeEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CharacterResponse {
    private Long id;
    private Integer number;
    private String name;
    private DistrictTypeEnum type;
    private List<AbilityResponse> abilities;
}
