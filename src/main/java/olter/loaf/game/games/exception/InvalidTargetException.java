package olter.loaf.game.games.exception;

import olter.loaf.game.cards.model.AbilityEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTargetException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> InvalidTargetException(Long targetId, AbilityEnum abilityEnum) {
        super("invalid target " + targetId + " for ability " + abilityEnum.getValue());
    }
}
