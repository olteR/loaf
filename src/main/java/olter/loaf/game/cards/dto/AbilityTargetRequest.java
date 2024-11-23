package olter.loaf.game.cards.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.DistrictTypeEnum;
import olter.loaf.game.games.model.ResourceTypeEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityTargetRequest {
    private Integer index;
    private Integer secondaryIndex;
    private List<Integer> indexes;
    private Long id;
    private Long secondaryId;
    private List<Long> targetIds;
    private ResourceTypeEnum resource;
    private DistrictTypeEnum type;
}
