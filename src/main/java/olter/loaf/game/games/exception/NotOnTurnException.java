package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotOnTurnException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NotOnTurnException(String code, Long userId) {
        super(userId + " is not on turn in " + code, HttpStatus.FORBIDDEN);
        setUserMessage("A játékban nem te vagy körön");
    }
}
