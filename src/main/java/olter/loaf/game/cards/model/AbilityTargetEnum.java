package olter.loaf.game.cards.model;

import lombok.Getter;

@Getter
public enum AbilityTargetEnum {
    NONE("NONE"), CHARACTER("CHARACTER"), PLAYER("PLAYER"), BUILT_DISTRICT("BUILT_DISTRICT"), OWN_CARDS(
        "OWN_CARDS"), PLAYER_AND_DISTRICT_TYPE("PLAYER_AND_DISTRICT_TYPE"), PLAYER_AND_DISTRICT_IN_HAND(
        "PLAYER_AND_DISTRICT_IN_HAND"),
    ;

    private final String value;

    AbilityTargetEnum(String value) {
        this.value = value;
    }
}
