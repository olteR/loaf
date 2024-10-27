package olter.loaf.game.config.model;

public enum ConfigTypeEnum {
    BASE_CARD("BASE_CARD"), DEFAULT_UNIQUE_DISTRICT("DEFAULT_UNIQUE_DISTRICT"), DEFAULT_CHARACTER(
        "DEFAULT_CHARACTER"), UPWARDS_CARDS_8C("UPWARDS_CARDS_8C"), UPWARDS_CARDS_9C("UPWARDS_CARDS_9C");

    private final String value;

    ConfigTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
