package olter.loaf.lobby.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class AlreadyJoinedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public <T> AlreadyJoinedException(Long lobbyId, Long userId) {
        super("user " + userId + " already in lobby " + lobbyId);
    }
}
