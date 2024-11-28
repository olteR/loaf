package olter.loaf.game.logs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CharacterPickResponse {
    private Long character;
    private Long picks;
}
