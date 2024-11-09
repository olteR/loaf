package olter.loaf.game.cards.model;

import lombok.Getter;

@Getter
public enum AbilityActivationEnum {
    MANUAL("MANUAL"), PASSIVE("PASSIVE");

    private final String value;

    AbilityActivationEnum(String value) {
        this.value = value;
    }
}
