package olter.loaf.users.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UsernameTakenException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UsernameTakenException(String username) {
        super(username + " username already in use", HttpStatus.CONFLICT);
        setUserMessage("A felhasználónév már foglalt");
    }
}
