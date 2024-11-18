package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.cards.model.AbilityEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InvalidDistricIndexException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDistricIndexException(Long playerId, Integer index) {
        super(playerId + " trying to build with invalid index " + index, HttpStatus.BAD_REQUEST);
        setUserMessage("Hibás kerület a kérésben");
    }
}
