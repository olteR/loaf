package olter.loaf.lobbies.lobby;

import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.lobbies.lobby.model.LobbyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LobbyMapper {
  LobbyListResponse lobbyEntityToListResponse(LobbyEntity entity);
}
