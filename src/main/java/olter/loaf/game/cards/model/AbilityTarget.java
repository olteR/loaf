package olter.loaf.game.cards.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.model.ResourceTypeEnum;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AbilityTarget {
    private Integer index;
    private List<Integer> indexes;
    private Long id;
    private List<Long> targetIds;
    private ResourceTypeEnum resource;
    private DistrictTypeEnum type;
}
