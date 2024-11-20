package olter.loaf.game.cards.model;

import lombok.Getter;

// @formatter:off
@Getter
public enum TargetEnum {
    NONE("NONE"),
    GOLD_OR_CARDS("GOLD_OR_CARDS"),
    GOLD_OR_CARDS_MULTIPLE("GOLD_OR_CARDS_MULTIPLE"),
    CHARACTER("CHARACTER"),
    PLAYER("PLAYER"),
    RICHEST_PLAYER("RICHEST_PLAYER"),
    BUILT_DISTRICT("BUILT_DISTRICT"),
    OWN_BUILT_DISTRICT("OWN_BUILT_DISTRICT"),
    CHEAP_BUILT_DISTRICT("CHEAP_BUILT_DISTRICT"),
    OWN_CARD("OWN_CARD"),
    OWN_CARDS("OWN_CARDS"),
    PLAYER_AND_DISTRICT_TYPE("PLAYER_AND_DISTRICT_TYPE"),
    PLAYER_AND_DISTRICT_IN_HAND("PLAYER_AND_DISTRICT_IN_HAND"),
    DISTRICT_AND_PLAYER_HELP("DISTRICT_AND_PLAYER_HELP"),
    WARRANTS("WARRANTS"),
    THREAT_MARKERS("THREAT_MARKERS"),
    SHUFFLE("SHUFFLE"),
    SELECTOR("SELECTOR");

    private final String value;

    TargetEnum(String value) {
        this.value = value;
    }
}
