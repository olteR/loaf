package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.games.model.GamePhaseEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidPhaseActionException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPhaseActionException(String code) {
        super("Invalid action for current game phase in " + code, HttpStatus.FORBIDDEN);
        setUserMessage("Ebben a fázisban ez a cselekvés nem megengedett");
    }
}
