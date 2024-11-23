package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.cards.model.AbilityEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class AlreadyUsedException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyUsedException(AbilityEnum abilityEnum, Long playerId) {
        super("Ability " + abilityEnum.getValue() + " for player " + playerId, HttpStatus.FORBIDDEN);
        setUserMessage("Ezt a képességet már használtad");
    }
}
