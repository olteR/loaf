package olter.loaf.logs.model;

public enum LogTypeEnum {
  SELECT_CHARACTER("SELECT_CHARACTER"),
  GATHER_RESOURCE("GATHER_RESOURCE"),
  BUILD("BUILD"),
  CHARACTER_ABILITY("CHARACTER_ABILITY"),
  CHARACTER_INCOME("CHARACTER_INCOME"),
  DISTRICT_ABILITY("DISTRICT_ABILITY");

  private final String value;

  LogTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
