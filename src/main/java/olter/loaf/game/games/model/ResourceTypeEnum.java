package olter.loaf.game.games.model;

import lombok.Getter;

@Getter
public enum ResourceTypeEnum {
    GOLD("GOLD"), CARDS("CARDS");

    private final String value;

    ResourceTypeEnum(String value) {
        this.value = value;
    }
}
