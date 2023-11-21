package olter.loaf.game.characters.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.districts.model.DistrictTypeEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class CharacterListResponse {
    private Long id;
    private Integer number;
    private String name;
    private DistrictTypeEnum districtTypeBonus;
}
