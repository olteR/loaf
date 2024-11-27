package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotEndedException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotEndedException(String code) {
        super(code + " still in progress", HttpStatus.FORBIDDEN);
        setUserMessage("A játék még folyamatban van");
    }
}
