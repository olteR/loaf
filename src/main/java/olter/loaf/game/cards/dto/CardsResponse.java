package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CardsResponse {
    List<CharacterResponse> characters;
    List<DistrictResponse> districts;
}
