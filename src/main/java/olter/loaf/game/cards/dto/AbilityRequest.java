package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.cards.model.AbilityTargetDto;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityRequest {
    private AbilityEnum ability;
    private String code;
    private AbilityTargetDto target;
}
