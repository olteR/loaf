package olter.loaf.lobbies;

import olter.loaf.game.games.GameMapper;
import olter.loaf.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.dto.LobbyDto;
import olter.loaf.lobbies.dto.LobbyListResponse;
import olter.loaf.lobbies.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {GameMapper.class})
public interface LobbyMapper {
    LobbyListResponse entityToListResponse(LobbyEntity entity);

    @Mapping(source = "game", target = "gameSettings")
    LobbyDetailsResponse entityToDetailsResponse(LobbyEntity entity);

    @Mapping(target = "password", ignore = true)
    LobbyEntity map(LobbyDto source, @MappingTarget LobbyEntity target);
}
