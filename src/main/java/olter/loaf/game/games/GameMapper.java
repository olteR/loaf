package olter.loaf.game.games;

import olter.loaf.game.games.dto.*;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.DistrictEmbeddable;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.users.model.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "gameId", source = "game.id")
    @Mapping(target = "characters", source = "game.characters")
    GameDetailsResponse lobbyToDetailsResponse(LobbyEntity entity);
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    GameStateResponse entitiesToStateResponse(GameEntity game, PlayerEntity player);

    PlayerDistrictResponse playerDistrictToResponse(DistrictEmbeddable districtEmbeddable);
}
