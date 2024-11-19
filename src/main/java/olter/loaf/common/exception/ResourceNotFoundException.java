package olter.loaf.common.exception;

import java.io.Serial;

public class ResourceNotFoundException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public <T> ResourceNotFoundException(String resource, T parameter) {
        super("No " + resource + " found with parameter: " + parameter);
        setUserMessage("Objektum nem található");
    }
}
