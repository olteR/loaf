package olter.loaf.game.games;

import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.PlayerMapper;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = PlayerMapper.class)
public interface GameMapper {

    @Mapping(target = "discardedCharacters", source = "game.upwardDiscard")
    GameDetailsResponse entityToDetailsResponse(GameEntity game, PlayerEntity player);

    @Mapping(target = "crownedPlayer", qualifiedByName = "playerToId")
    @Mapping(target = "characters", qualifiedByName = "characterToId")
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    @Named("playerToId")
    default Long playerToId(PlayerEntity player) {
        if (player == null) return null;
        return player.getUserId();
    }

    @Named("characterToId")
    default Long characterToId(CharacterEntity character) {
        if (character == null) return null;
        return character.getId();
    }
}
