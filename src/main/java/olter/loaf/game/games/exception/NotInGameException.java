package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotInGameException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotInGameException(String code, Long userId) {
        super("User " + userId + " unauthorized to access " + code, HttpStatus.UNAUTHORIZED);
        setUserMessage("Nincs hozzáférésed ehhez a játékhoz");
    }
}
