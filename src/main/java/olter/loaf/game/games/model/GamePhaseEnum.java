package olter.loaf.game.games.model;

import lombok.Getter;

@Getter
public enum GamePhaseEnum {
    // @formatter:off
    NOT_STARTED("NOT_STARTED"),
    SELECTION("SELECTION"),
    RESOURCE("RESOURCE"),
    TURN("TURN"),
    ENDED("ENDED");
    // @formatter:on

    private final String value;

    GamePhaseEnum(String value) {
        this.value = value;
    }

}
