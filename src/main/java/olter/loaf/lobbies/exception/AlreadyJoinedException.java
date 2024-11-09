package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;

import java.io.Serial;

public class AlreadyJoinedException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyJoinedException(String code, Long userId) {
        super("User " + userId + " already in lobby " + code);
        setUserMessage("Már benne vagy ebben a lobbiban");
    }
}
