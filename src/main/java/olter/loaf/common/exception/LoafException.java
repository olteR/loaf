package olter.loaf.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class LoafException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    @Setter
    private String userMessage;

    public LoafException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LoafException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
