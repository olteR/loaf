package olter.loaf.game.games.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.games.model.ResourceTypeEnum;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResourceGatherResponse {
    ResourceTypeEnum resource;
    Integer amount;
}
