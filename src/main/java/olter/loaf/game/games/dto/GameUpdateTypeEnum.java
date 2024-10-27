package olter.loaf.game.games.dto;

import lombok.Getter;

@Getter
public enum GameUpdateTypeEnum {
    NEXT_PLAYER("NEXT_PLAYER"), PLAYER_TURN("PLAYER_TURN"), CHARACTER_REVEAL("CHARACTER_REVEAL"), RESOURCE_COLLECTION(
        "RESOURCE_COLLECTION"), BUILD("BUILD"), CHARACTER_ABILITY("CHARACTER_ABILITY");

    private final String value;

    GameUpdateTypeEnum(String value) {
        this.value = value;
    }

}
