package olter.loaf.game.config.model;

import lombok.Getter;

@Getter
public enum ConfigTypeEnum {
    // @formatter:off
    BASE_CARD("BASE_CARD"),
    DEFAULT_UNIQUE_DISTRICT("DEFAULT_UNIQUE_DISTRICT"),
    DEFAULT_CHARACTER("DEFAULT_CHARACTER");

    private final String value;

    ConfigTypeEnum(String value) {
        this.value = value;
    }
}
