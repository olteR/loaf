package olter.loaf.users.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class LoginException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginException(String name) {
        super("Invalid login attempt for username: " + name, HttpStatus.UNAUTHORIZED);
        setUserMessage("Helytelen felhasználónév vagy jelszó");
    }
}
