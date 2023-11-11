package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameSettingsResponse {
    private Long crownedPlayer;
    private List<Long> characters;
    private List<Long> uniqueCards;
}
