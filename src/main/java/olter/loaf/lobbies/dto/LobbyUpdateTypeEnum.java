package olter.loaf.lobbies.dto;

import lombok.Getter;

// @formatter:off
@Getter
public enum LobbyUpdateTypeEnum {
    JOIN("JOIN"),
    LEAVE("LEAVE"),
    EDIT("EDIT"),
    OWNER("OWNER"),
    KICK("KICK"),
    CHARACTERS("CHARACTERS"),
    DISTRICTS("DISTRICTS"),
    CROWN("CROWN"),
    DELETE("DELETE"),
    START("START");

    private final String value;

    LobbyUpdateTypeEnum(String value) {
        this.value = value;
    }

}
