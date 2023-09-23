package olter.loaf.lobbies.lobby;

import olter.loaf.lobbies.lobby.dto.LobbyCreationRequest;
import olter.loaf.lobbies.lobby.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.lobbies.lobby.model.LobbyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LobbyMapper {
  LobbyListResponse entityToListResponse(LobbyEntity entity);
  LobbyDetailsResponse entityToDetailsResponse(LobbyEntity entity);
  LobbyEntity creationRequestToEntity(LobbyCreationRequest request);
}
