package olter.loaf.game.games.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InvalidPhaseActionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> InvalidPhaseActionException(Long gameId) {
        super("Invalid action for current game phase in " + gameId);
    }
}
