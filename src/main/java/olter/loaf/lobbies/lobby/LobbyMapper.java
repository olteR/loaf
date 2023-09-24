package olter.loaf.lobbies.lobby;

import olter.loaf.lobbies.lobby.dto.LobbyCreationRequest;
import olter.loaf.lobbies.lobby.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.lobbies.lobby.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LobbyMapper {
  LobbyListResponse entityToListResponse(LobbyEntity entity);

  LobbyDetailsResponse entityToDetailsResponse(LobbyEntity entity);

  @Mapping(target = "password", ignore = true)
  LobbyEntity map(LobbyCreationRequest source, @MappingTarget LobbyEntity target);
}
