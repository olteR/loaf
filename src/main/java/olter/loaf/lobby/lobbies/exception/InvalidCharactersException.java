package olter.loaf.lobby.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidCharactersException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T> InvalidCharactersException(String code) {
        super("Invalid character list in request for lobby " + code);
    }
}
