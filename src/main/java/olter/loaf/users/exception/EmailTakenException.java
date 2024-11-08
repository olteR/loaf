package olter.loaf.users.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class EmailTakenException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EmailTakenException(String email) {
        super(email + " email already in use", HttpStatus.CONFLICT);
        setUserMessage("Az E-mail cím már foglalt");
    }
}
