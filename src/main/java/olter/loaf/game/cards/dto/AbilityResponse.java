package olter.loaf.game.cards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.TargetEnum;
import olter.loaf.game.cards.model.ActivationEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityResponse {
    @JsonProperty("enum")
    private String value;
    private ActivationEnum type;
    private TargetEnum target;
    private String description;
    private List<String> icons;
}
