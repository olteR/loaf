package olter.loaf.lobby.lobbies.dto;

import lombok.Getter;

@Getter
public enum LobbyUpdateTypeEnum {
    JOIN("JOIN"), LEAVE("LEAVE"), OWNER("OWNER"), KICK("KICK"), CHARACTERS("CHARACTERS"), DISTRICTS("DISTRICTS"), CROWN(
        "CROWN"), DELETE("DELETE"), START("START");

    private final String value;

    LobbyUpdateTypeEnum(String value) {
        this.value = value;
    }

}
