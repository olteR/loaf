package olter.loaf.game.games.exception;

import olter.loaf.common.exception.LoafException;
import olter.loaf.game.cards.model.AbilityEnum;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class NotEnoughGoldException extends LoafException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotEnoughGoldException(Long playerId, Long districtId) {
        super(playerId + " does not have enough gold to build " + districtId, HttpStatus.FORBIDDEN);
        setUserMessage("Nincs elég aranyad a megépítéshez");
    }

    public NotEnoughGoldException(Long playerId, AbilityEnum ability) {
        super(playerId + " does not have enough gold to use " + ability, HttpStatus.FORBIDDEN);
        setUserMessage("Nincs elég aranyad a használathoz");
    }
}
