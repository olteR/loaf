package olter.loaf.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public <T> ResourceNotFoundException(String resource, T parameter) {
    super("no " + resource + " found with parameter: " + parameter);
  }
}
