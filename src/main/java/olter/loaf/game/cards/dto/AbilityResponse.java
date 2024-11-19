package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.AbilityTargetEnum;
import olter.loaf.game.cards.model.AbilityUsageEnum;
import olter.loaf.game.cards.model.ActivationEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityResponse {
    private Long id;
    private ActivationEnum type;
    private AbilityUsageEnum useRule;
    private AbilityTargetEnum target;
    private String description;
    private List<String> icons;
}
