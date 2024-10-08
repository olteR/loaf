package olter.loaf.game.games.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class CorruptedGameException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> CorruptedGameException(Long gameId) {
        super("Corrupted data in game " + gameId);
    }
}
