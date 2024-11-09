package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NoPrivilegeException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NoPrivilegeException(String code, Long userId) {
        super(userId + " has owner privilege for lobby " + code, HttpStatus.FORBIDDEN);
        setUserMessage("Ehhez a művelethez nincs jogod ebben a lobbiban");
    }
}
