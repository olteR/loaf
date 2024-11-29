package olter.loaf.common.exception;

import java.io.Serial;

public class ResourceNotFoundException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T, U> ResourceNotFoundException(T resource, U parameter) {
        super("No " + resource.getClass().getName() + " found with parameter: " + parameter);
        setUserMessage("Objektum nem található");
    }
}
