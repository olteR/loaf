package olter.loaf.game.games.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.dto.CharacterResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GameSettingsResponse {
    private Long crownedPlayer;
    private List<CharacterResponse> characters;
    private List<Long> uniqueDistricts;
}
