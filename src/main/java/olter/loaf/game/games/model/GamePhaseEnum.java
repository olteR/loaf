package olter.loaf.game.games.model;

import lombok.Getter;

@Getter
public enum GamePhaseEnum {
    NOT_STARTED("NOT_STARTED"),
    SELECTION("SELECTION"),
    RESOURCE("RESOURCE"),
    TURN("TURN"),
    FINAL_TURN("FINAL_TURN"),
    ENDED("ENDED");

    private final String value;

    GamePhaseEnum(String value) {
        this.value = value;
    }

}
