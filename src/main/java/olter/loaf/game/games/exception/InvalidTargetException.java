package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.cards.model.AbilityEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidTargetException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidTargetException(AbilityEnum abilityEnum) {
        super("Invalid targeting for ability " + abilityEnum.getValue(), HttpStatus.BAD_REQUEST);
        setUserMessage("Nem megfelelő célpont ehhez a képességhez");
    }
}
