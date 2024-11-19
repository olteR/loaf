package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.AbilityEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityRequest {
    private AbilityEnum ability;
    private String code;
}
