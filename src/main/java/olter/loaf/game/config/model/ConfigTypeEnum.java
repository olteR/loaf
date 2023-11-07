package olter.loaf.game.config.model;

public enum ConfigTypeEnum {
  BASE_CARD("BASE_CARD"),
  DEFAULT_UNIQUE_DISTRICT("DEFAULT_UNIQUE_DISTRICT"),
  DEFAULT_CHARACTER("DEFAULT_CHARACTER");

  private final String value;

  ConfigTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
