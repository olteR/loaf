package olter.loaf.statistics;

import lombok.Getter;

@Getter
public enum LogTypeEnum {
    // @formatter:off
    SELECT_CHARACTER("SELECT_CHARACTER"),
    GATHER_RESOURCE("GATHER_RESOURCE"),
    BUILD("BUILD"),
    ABILITY_USE("ABILITY_USE");
    // @formatter:on

    private final String value;

    LogTypeEnum(String value) {
        this.value = value;
    }

}
