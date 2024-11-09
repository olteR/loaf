package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.AbilityTargetEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityResponse {
    private String ability;
    private AbilityTargetEnum target;
}
