package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class BuildLimitException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BuildLimitException(Long playerId) {
        super(playerId + " build limit reached", HttpStatus.FORBIDDEN);
        setUserMessage("Ebben a körben nem építhetsz több kerületet");
    }
}
