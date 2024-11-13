package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidPasswordException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPasswordException(String code, Long userId) {
        super("User " + userId + " gave invalid password in lobby " + code, HttpStatus.BAD_REQUEST);
        setUserMessage("Hibás jelszó!");
    }
}
