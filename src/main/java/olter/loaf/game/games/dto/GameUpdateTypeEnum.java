package olter.loaf.game.games.dto;

import lombok.Getter;

// @formatter:off
@Getter
public enum GameUpdateTypeEnum {
    NEXT_PLAYER("NEXT_PLAYER"),
    PLAYER_TURN("PLAYER_TURN"),
    CHARACTER_REVEAL("CHARACTER_REVEAL"),
    RESOURCE_COLLECTION("RESOURCE_COLLECTION"),
    BUILD("BUILD"),
    USE_ABILITY("USE_ABILITY"),
    NEW_TURN("NEW_TURN");

    private final String value;

    GameUpdateTypeEnum(String value) {
        this.value = value;
    }

}
