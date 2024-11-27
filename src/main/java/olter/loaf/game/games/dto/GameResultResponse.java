package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.players.dto.PlayerResultResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameResultResponse {
    private Integer turn;
    private List<CharacterResponse> characters;
    private List<Long> uniqueDistricts;
    private List<PlayerResultResponse> players;
}
