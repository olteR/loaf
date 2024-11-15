package olter.loaf.game.cards.model;

import lombok.Getter;

// @formatter:off
@Getter
public enum ActivationEnum {
    MANUAL("MANUAL"),
    START_OF_TURN("START_OF_TURN"),
    END_OF_TURN("END_OF_TURN"),
    RESOURCE_GATHERING("RESOURCE_GATHERING"),
    AFTER_GATHERING("AFTER_GATHERING"),
    BEFORE_BUILD("BEFORE_BUILD"),
    ON_BUILD("ON_BUILD"),
    AFTER_BUILD("AFTER_BUILD"),
    END_OF_GAME("END_OF_GAME"),
    NONE("NONE");

    private final String value;

    ActivationEnum(String value) {
        this.value = value;
    }
}
