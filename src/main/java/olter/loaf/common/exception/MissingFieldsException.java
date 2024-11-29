package olter.loaf.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MissingFieldsException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T> MissingFieldsException(T resource) {
        super("Missing fields in request " + resource.getClass().getName(), HttpStatus.BAD_REQUEST);
        setUserMessage("Hiányzó mezők a kérésben");
    }
}
