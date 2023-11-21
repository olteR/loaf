package olter.loaf.logs.model;

public enum LogTypeEnum {
    SELECT_CHARACTER("SELECT_CHARACTER"), // targetId
    GATHER_RESOURCE("GATHER_RESOURCE"),
    BUILD("BUILD"), // targetId
    CHARACTER_ABILITY("CHARACTER_ABILITY"), // targetId, sourceId
    CHARACTER_INCOME("CHARACTER_INCOME"),
    DISTRICT_ABILITY("DISTRICT_ABILITY"); // targetId, sourceId

    private final String value;

    LogTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
