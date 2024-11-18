package olter.loaf.game.games;

import olter.loaf.game.cards.CardMapper;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.PlayerMapper;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, CardMapper.class})
public interface GameMapper {

    @Mapping(target = "currentPlayer", qualifiedByName = "playerToId")
    @Mapping(target = "crownedPlayer", qualifiedByName = "playerToId")
    @Mapping(target = "discardedCharacters", source = "game.upwardDiscard")
    @Mapping(target = "currentCharacter", source = "playerCharacter")
    GameDetailsResponse entityToDetailsResponse(GameEntity game, PlayerEntity player, Integer playerCharacter, String code);

    @Mapping(target = "crownedPlayer", qualifiedByName = "playerToUserId")
    @Mapping(target = "characters", qualifiedByName = "characterToId")
    GameSettingsResponse entityToSettingsResponse(GameEntity entity);

    @Named("playerToId")
    default Long playerToId(PlayerEntity player) {
        if (player == null) return null;
        return player.getId();
    }

    @Named("playerToUserId")
    default Long playerToUserId(PlayerEntity player) {
        if (player == null) return null;
        return player.getUserId();
    }

    @Named("characterToId")
    default Long characterToId(CharacterEntity character) {
        if (character == null) return null;
        return character.getId();
    }
}
