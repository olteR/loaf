package olter.loaf.lobbies;

import olter.loaf.lobbies.dto.LobbyCreationRequest;
import olter.loaf.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.dto.LobbyListResponse;
import olter.loaf.lobbies.model.LobbyEntity;
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
