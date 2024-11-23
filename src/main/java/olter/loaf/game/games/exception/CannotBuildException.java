package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;

import java.io.Serial;

public class CannotBuildException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CannotBuildException(Long playerId, Long districtId) {
        super("Player " + playerId + " cannot build " + districtId);
        setUserMessage("Ezt a kerületet nem építheted meg");
    }
}
