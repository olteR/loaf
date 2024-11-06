package olter.loaf.lobby.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoPrivilegeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> NoPrivilegeException(String code, Long userId) {
        super(userId + " has owner privilege for lobby " + code);
    }
}
