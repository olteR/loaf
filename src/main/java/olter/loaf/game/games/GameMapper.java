package olter.loaf.game.games;

import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.games.dto.*;
import olter.loaf.game.games.model.GameCharacterEmbeddable;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.model.DistrictEmbeddable;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "gameId", source = "game.id")
    @Mapping(target = "characters", source = "game.characters", qualifiedByName = "characterToId")
    GameDetailsResponse lobbyToDetailsResponse(LobbyEntity entity);

    @Mapping(target = "characters", qualifiedByName = "characterToId")
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    @Mapping(target = "discardedCharacters", source = "game.upwardDiscard")
    @Mapping(target = "currentCharacter")
    GameStateResponse entitiesToStateResponse(GameEntity game, PlayerEntity player);

    @Mapping(target = "handSize", expression = "java(player.getHand().size())")
    PublicPlayerResponse playerEntityToResponse(PlayerEntity player);

    PlayerDistrictResponse playerDistrictToResponse(DistrictEmbeddable districtEmbeddable);

    @Mapping(target = "characterId", source = "configId")
    @Mapping(target = "number", source = "configValue")
    GameCharacterEmbeddable configToCharacterEmbeddable(ConfigEntity config);

    @Named("characterToId")
    default Long characterToId(GameCharacterEmbeddable character) {
        if (character == null) return null;
        return character.getCharacterId();
    }
}
