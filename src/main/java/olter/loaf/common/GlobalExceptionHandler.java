package olter.loaf.common;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoafException.class)
    public ResponseEntity<String> handleMyCustomException(LoafException ex) {
        return new ResponseEntity<>(ex.getUserMessage(), ex.getStatus());
    }
}
