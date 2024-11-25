package olter.loaf.game.players.model;

import lombok.Getter;

@Getter
public enum DurationEnum {
    INDEFINITE("INDEFINITE"), END_OF_TURN("END_OF_TURN");

    private final String value;

    DurationEnum(String value) {
        this.value = value;
    }
}
