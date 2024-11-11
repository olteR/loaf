package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.AbilityActivationEnum;
import olter.loaf.game.cards.model.AbilityTargetEnum;
import olter.loaf.game.cards.model.AbilityUsageEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityResponse {
    private Long id;
    private String name;
    private String icon;
    private AbilityActivationEnum type;
    private AbilityUsageEnum useRule;
    private AbilityTargetEnum target;
    private String description;
}
