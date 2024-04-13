package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.model.GamePhaseEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameStateResponse {
    private Long crownedPlayer;
    private Long currentPlayer;
    private Integer turn;
    private GamePhaseEnum phase;
    private Integer gold;
    private Long currentCharacter;
    private List<Long> hand;
    private List<Integer> discardedCharacters;
    private List<Integer> unavailableCharacters;
    private List<PublicPlayerResponse> players;
}
