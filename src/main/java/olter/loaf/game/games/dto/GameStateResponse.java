package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.model.GamePhaseEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class GameStateResponse {
    private Long crownedPlayer;
    private Long currentPlayer;
    private Integer turn;
    private GamePhaseEnum phase;
}
