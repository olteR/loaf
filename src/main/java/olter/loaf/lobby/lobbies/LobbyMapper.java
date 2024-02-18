package olter.loaf.lobby.lobbies;

import olter.loaf.game.games.GameMapper;
import olter.loaf.lobby.lobbies.dto.LobbyCreationRequest;
import olter.loaf.lobby.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobby.lobbies.dto.LobbyListResponse;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = GameMapper.class)
public interface LobbyMapper {
    LobbyListResponse entityToListResponse(LobbyEntity entity);

    @Mapping(source = "game", target = "gameSettings")
    LobbyDetailsResponse entityToDetailsResponse(LobbyEntity entity);

    @Mapping(target = "password", ignore = true)
    LobbyEntity map(LobbyCreationRequest source, @MappingTarget LobbyEntity target);
}
