package olter.loaf.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoPrivilegeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public <T> NoPrivilegeException(Long lobbyId, Long userId) {
        super(userId + " has owner privilege for lobby" + lobbyId);
    }
}
