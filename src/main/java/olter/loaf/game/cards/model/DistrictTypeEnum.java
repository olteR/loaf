package olter.loaf.game.cards.model;

import lombok.Getter;

@Getter
public enum DistrictTypeEnum {
    NOBLE("NOBLE"), RELIGIOUS("RELIGIOUS"), TRADE("TRADE"), MILITARY("MILITARY"), UNIQUE("UNIQUE");

    private final String value;

    DistrictTypeEnum(String value) {
        this.value = value;
    }
}
