package olter.loaf.lobby.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDistrictsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T> InvalidDistrictsException(String code) {
        super("Invalid district list in request for lobby " + code);
    }
}
