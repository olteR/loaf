package olter.loaf.lobbies.exception;

import olter.loaf.common.exception.LoafException;

import java.io.Serial;

public class GameInProgressException extends LoafException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GameInProgressException(String code) {
        super(code + " already in progress!");
        setUserMessage("A játék már folyamatban van");
    }
}
