package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotEnoughGoldException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotEnoughGoldException(Long playerId, Long districtId) {
        super(playerId + " does not have enough gold to build " + districtId, HttpStatus.BAD_REQUEST);
        setUserMessage("Nincs elég aranyad a megépítéshez");
    }
}
