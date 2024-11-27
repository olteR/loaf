package olter.loaf.game.players.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.dto.DistrictResponse;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PublicPlayerResponse {
    private Long id;
    private Long userId;
    private String name;
    private Integer gold;
    private Integer points;
    private Integer handSize;
    private Integer character;
    private Integer order;
    private List<ConditionResponse> conditions;
    private List<DistrictResponse> districts;
}
