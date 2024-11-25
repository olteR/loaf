package olter.loaf.game.players.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.ActivationEnum;
import olter.loaf.game.players.model.DurationEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class ConditionResponse {
    private String value;
    private String name;
    private String icon;
    private ActivationEnum activation;
    private DurationEnum duration;
    private String description;
}
