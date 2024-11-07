package olter.loaf.lobby.lobbies;

import olter.loaf.game.games.GameMapper;
import olter.loaf.lobby.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobby.lobbies.dto.LobbyListResponse;
import olter.loaf.lobby.lobbies.dto.LobbyRequest;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {GameMapper.class})
public interface LobbyMapper {
    LobbyListResponse entityToListResponse(LobbyEntity entity);

    @Mapping(source = "game", target = "gameSettings")
    LobbyDetailsResponse entityToDetailsResponse(LobbyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "members", ignore = true)
    LobbyEntity map(LobbyRequest source, @MappingTarget LobbyEntity target);
}
