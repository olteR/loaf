package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.players.model.ConditionEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidActivationException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidActivationException(Long playerId, AbilityEnum abilityEnum) {
        super(playerId + " invalid activation for ability " + abilityEnum.getValue(), HttpStatus.BAD_REQUEST);
        setUserMessage("Ezt a képességet nem használhatod ilyen módon");
    }

    public InvalidActivationException(Long playerId, ConditionEnum conditionEnum) {
        super(playerId + " invalid activation for ability " + conditionEnum.getValue());
        setUserMessage("Ezt a képességet nem használhatod ilyen módon");
    }
}
