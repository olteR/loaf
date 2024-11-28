package olter.loaf.game.players.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class PlayerResults {
    private Integer placement;
    private Integer districtPoints = 0;
    private Integer bonusPoints = 0;
    private Boolean finished = false;
    private Boolean finishedFirst = false;
    private Boolean hasAllTypes = false;

    public Integer getTotalPoints() {
        int score = districtPoints + bonusPoints;
        if (finishedFirst) {
            score += 4;
        } else if (finished) {
            score += 2;
        }
        if (hasAllTypes) {
            score += 3;
        }
        return score;
    }

    public void giveDistrictPoints(Integer points) {
        districtPoints += points;
    }

    public void takeDistrictPoints(Integer points) {
        districtPoints -= points;
    }

    public void giveBonusPoints(Integer points) {
        bonusPoints += points;
    }
}
