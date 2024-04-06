package olter.loaf.game.games;

import olter.loaf.game.games.dto.*;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.DistrictEmbeddable;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "gameId", source = "game.id")
    @Mapping(target = "characters", source = "game.characters")
    GameDetailsResponse lobbyToDetailsResponse(LobbyEntity entity);

    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    GameStateResponse entitiesToStateResponse(GameEntity game, PlayerEntity player);

    @Mapping(target = "handSize", expression = "java(player.getHand().size())")
    PublicPlayerResponse playerEntityToResponse(PlayerEntity player);

    PlayerDistrictResponse playerDistrictToResponse(DistrictEmbeddable districtEmbeddable);
}
