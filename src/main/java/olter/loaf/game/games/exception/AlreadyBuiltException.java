package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class AlreadyBuiltException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AlreadyBuiltException(Long playerId, Long districtId) {
        super(playerId + " already has district " + districtId, HttpStatus.BAD_REQUEST);
        setUserMessage("Már van ilyen kerület a városodban");
    }
}
