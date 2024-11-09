package olter.loaf.common;

import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.LoafException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LoafException.class)
    public ResponseEntity<String> handleMyCustomException(LoafException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getUserMessage(), ex.getStatus());
    }
}
