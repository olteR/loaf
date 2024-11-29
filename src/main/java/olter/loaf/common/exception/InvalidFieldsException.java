package olter.loaf.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidFieldsException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T> InvalidFieldsException(T resource) {
        super("Invalid fields in request " + resource.getClass().getName(), HttpStatus.BAD_REQUEST);
        setUserMessage("Hibás mezők a kérésben");
    }
}
