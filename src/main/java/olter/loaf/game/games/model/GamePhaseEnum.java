package olter.loaf.game.games.model;

import lombok.Getter;

@Getter
public enum GamePhaseEnum {
    NOT_STARTED("NOT_STARTED"),
    SELECTION("SELECTION"),
    TURN("TURN"),
    END_TURN("END_TURN"),
    ENDED("ENDED");

    private final String value;

    GamePhaseEnum(String value) {
        this.value = value;
    }

}
