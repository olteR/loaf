package olter.loaf.game.logs.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PreviousGameResponse {
    private String code;
    private String name;
    private Integer placement;
    private Integer points;
    private List<String> players;
}
