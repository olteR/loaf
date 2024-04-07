package olter.loaf.game.games.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GameUpdateDto {
    GameUpdateTypeEnum type;
    Object change;
}
