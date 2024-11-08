package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;

import java.io.Serial;

public class CorruptedGameException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CorruptedGameException(String code) {
        super("Corrupted data in game " + code);
        setUserMessage("Sérült adat a játékban");
    }
}
