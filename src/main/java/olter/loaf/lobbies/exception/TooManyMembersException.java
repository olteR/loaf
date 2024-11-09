package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;

import java.io.Serial;

public class TooManyMembersException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TooManyMembersException(String code) {
        super(code + " has too many members for the given operation");
        setUserMessage("A lobbiban túl sok felhasználó van ehhez a művelethez");
    }
}
