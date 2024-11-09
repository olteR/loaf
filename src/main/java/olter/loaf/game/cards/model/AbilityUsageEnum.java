package olter.loaf.game.cards.model;

import lombok.Getter;

@Getter
public enum AbilityUsageEnum {
    AND("AND"), OR("OR"), MUST("MUST");

    private final String value;

    AbilityUsageEnum(String value) {
        this.value = value;
    }
}
