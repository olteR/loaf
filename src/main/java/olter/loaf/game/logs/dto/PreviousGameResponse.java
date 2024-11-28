package olter.loaf.game.logs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PreviousGameResponse {
    private String code;
    private Integer placement;
}
