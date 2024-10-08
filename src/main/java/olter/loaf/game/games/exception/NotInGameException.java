package olter.loaf.game.games.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotInGameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> NotInGameException(Long gameId, Long userId) {
        super("user " + userId + " unauthorized to access " + gameId);
    }
}
