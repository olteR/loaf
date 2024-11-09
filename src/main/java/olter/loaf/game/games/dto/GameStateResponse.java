package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.model.GamePhaseEnum;
import olter.loaf.game.players.dto.PublicPlayerResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameStateResponse {
    private PublicPlayerResponse crownedPlayer;
    private PublicPlayerResponse currentPlayer;
    private Integer turn;
    private GamePhaseEnum phase;
    private Integer gold;
    private Integer currentCharacter;
    private List<Long> hand;
    private List<Integer> discardedCharacters;
    private List<Integer> unavailableCharacters;
    private List<Integer> skippedCharacters;
    private List<PublicPlayerResponse> players;
}
