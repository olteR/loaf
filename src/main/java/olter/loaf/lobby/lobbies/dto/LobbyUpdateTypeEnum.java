package olter.loaf.lobby.lobbies.dto;

import lombok.Getter;

@Getter
public enum LobbyUpdateTypeEnum {
    JOIN("JOIN"),
    LEAVE("LEAVE"),
    KICK("KICK"),
    SETTINGS("SETTINGS"),
    OWNER("OWNER"),
    DELETE("DELETE"),
    START("START");

    private final String value;

    LobbyUpdateTypeEnum(String value) {
        this.value = value;
    }

}
