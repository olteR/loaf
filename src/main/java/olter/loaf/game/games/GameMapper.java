package olter.loaf.game.games;

import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.game.games.dto.GameStateResponse;
import olter.loaf.game.games.dto.PlayerDistrictResponse;
import olter.loaf.game.games.dto.PublicPlayerResponse;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.DistrictEmbeddable;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.users.model.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    GameStateResponse entitiesToStateResponse(GameEntity game, PlayerEntity player);

    PlayerDistrictResponse playerDistrictToResponse(DistrictEmbeddable districtEmbeddable);
}
