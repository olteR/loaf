package olter.loaf.game.games;

import olter.loaf.game.cards.CardMapper;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameResultResponse;
import olter.loaf.game.games.dto.GameSettingsResponse;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.players.PlayerMapper;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Comparator;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class, CardMapper.class})
public interface GameMapper {

    @Mapping(target = "currentPlayer", qualifiedByName = "playerToId")
    @Mapping(target = "warrantedCharacter", source = "game", qualifiedByName = "fillWarrantedCharacter")
    @Mapping(target = "threatenedCharacter", source = "game", qualifiedByName = "fillThreatenedCharacter")
    @Mapping(target = "character", source = "player.characterNumber")
    @Mapping(target = "discardedCharacters", source = "game.upwardDiscard")
    GameDetailsResponse entityToDetailsResponse(GameEntity game, PlayerEntity player, String code);

    GameResultResponse entityToResultResponse(GameEntity game);

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

    @Named("fillWarrantedCharacter")
    default Integer fillWarrantedCharacter(GameEntity game) {
        return game.getPlayers().stream().filter(player -> player.hasCondition(ConditionEnum.WARRANTED_SIGNED))
            .map(PlayerEntity::getCharacterNumber).findFirst().orElse(null);
    }

    @Named("fillThreatenedCharacter")
    default Integer fillThreatenedCharacter(GameEntity game) {
        return game.getPlayers().stream().filter(player -> player.hasCondition(ConditionEnum.THREATENED_REAL))
            .map(PlayerEntity::getCharacterNumber).findFirst().orElse(null);
    }
}
