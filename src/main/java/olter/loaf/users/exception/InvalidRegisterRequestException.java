package olter.loaf.users.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidRegisterRequestException extends LoafException {
  @Serial
  private static final long serialVersionUID = 1L;

  public InvalidRegisterRequestException(String name) {
    super("Invalid register attempt with name: " + name, HttpStatus.BAD_REQUEST);
    setUserMessage("Helytelen regisztrálási adatok");
    }
}
