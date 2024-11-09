package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidDistrictsException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDistrictsException(String code) {
        super("Invalid district list in request for lobby " + code, HttpStatus.BAD_REQUEST);
        setUserMessage("Nem megfelelő kerület lista");
    }
}
