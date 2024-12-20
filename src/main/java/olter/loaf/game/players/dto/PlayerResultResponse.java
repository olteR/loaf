package olter.loaf.game.players.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.dto.DistrictResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PlayerResultResponse {
    private Long id;
    private String name;
    private Integer gold;
    private Integer placement;
    private Integer points;
    private Integer districtPoints;
    private Integer bonusPoints;
    private Boolean finished;
    private Boolean finishedFirst;
    private Boolean hasAllTypes;
    private List<DistrictResponse> hand;
    private List<DistrictResponse> districts;
}
