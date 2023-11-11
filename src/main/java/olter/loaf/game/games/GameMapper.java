package olter.loaf.game.games;

import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.game.games.model.GameEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);
}
