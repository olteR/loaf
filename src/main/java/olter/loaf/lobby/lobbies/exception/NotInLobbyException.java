package olter.loaf.lobby.lobbies.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotInLobbyException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotInLobbyException(String code, Long userId) {
        super("user " + userId + " unauthorized to access " + code, HttpStatus.UNAUTHORIZED);
        setUserMessage("Ehhez a lobbihoz nincs hozzáférésed");
    }
}
