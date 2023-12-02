package olter.loaf.lobbies.dto;

import lombok.Getter;

@Getter
public enum LobbyUpdateTypeEnum {
    JOIN("JOIN"),
    LEAVE("LEAVE"),
    KICK("KICK"),
    SETTINGS("SETTINGS"),
    OWNER("OWNER");

    private final String value;

    LobbyUpdateTypeEnum(String value) {
        this.value = value;
    }

}
