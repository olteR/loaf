package olter.loaf.game.logs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserStatisticsResponse {
    private Integer gamesPlayed;
    private Integer gamesWon;
    private Double averagePlacement;
    private Double goldCardsRatio;
    private Integer mostPickedNumber;
    private Integer mostPickedCharacter;
}
