package olter.loaf.lobby.lobbies.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidCharactersException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCharactersException(String code) {
        super("Invalid character list in request for lobby " + code, HttpStatus.BAD_REQUEST);
        setUserMessage("Nem megfelelő karakter lista");
    }
}
