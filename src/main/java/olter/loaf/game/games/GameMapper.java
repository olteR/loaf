package olter.loaf.game.games;

import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.games.dto.*;
import olter.loaf.game.games.model.GameCharacterEmbeddable;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.DistrictEmbeddable;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.users.model.UserEntity;
import olter.loaf.users.model.UserRepository;
import org.apache.catalina.User;
import org.mapstruct.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserRepository.class)
public interface GameMapper {

    @Mapping(target = "discardedCharacters", source = "game.upwardDiscard")
    GameDetailsResponse entitiesToDetailsResponse(GameEntity game, PlayerEntity player);

    @Mapping(target = "crownedPlayer", qualifiedByName = "playerToId")
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    @Mapping(target = "handSize", expression = "java(player.getHand().size())")
    PublicPlayerResponse playerEntityToResponse(PlayerEntity player);

    PlayerDistrictResponse playerDistrictToResponse(DistrictEmbeddable districtEmbeddable);

    @Mapping(target = "characterId", source = "configId")
    @Mapping(target = "number", source = "configValue")
    GameCharacterEmbeddable configToCharacterEmbeddable(ConfigEntity config);

    @Named("playerToId")
    default Long playerToId(PlayerEntity player) {
        if (player == null) return null;
        return player.getUserId();
    }
}
