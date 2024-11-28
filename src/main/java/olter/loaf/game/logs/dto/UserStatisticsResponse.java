package olter.loaf.game.logs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserStatisticsResponse {
    private Integer gamesPlayed;
    private Long gamesWon;
    private Double averagePlacement;
    private Long goldChosen;
    private Long cardsChosen;
    private List<CharacterPickResponse> numberPicks;
    private List<CharacterPickResponse> characterPicks;
    private List<PreviousGameResponse> previousGames;
}
