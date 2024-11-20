package olter.loaf.game.players.model;

import lombok.Getter;

@Getter
public enum ConditionDurationEnum {
    INDEFINITE("INDEFINITE"), END_OF_TURN("END_OF_TURN");

    private final String value;

    ConditionDurationEnum(String value) {
        this.value = value;
    }
}
