package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.DistrictTypeEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class DistrictResponse {
    private Long id;
    private String name;
    private String cardName;
    private String cardText;
    private DistrictTypeEnum type;
    private Integer cost;
}
