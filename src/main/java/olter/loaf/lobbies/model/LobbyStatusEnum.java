package olter.loaf.lobbies.model;

import lombok.Getter;

@Getter
public enum LobbyStatusEnum {
    CREATED("CREATED"),
    ONGOING("ONGOING"),
    ENDED("ENDED");

    private final String value;

    LobbyStatusEnum(String value) {
        this.value = value;
    }

}
