package olter.loaf.game.players;

import olter.loaf.game.players.dto.PublicPlayerResponse;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "currentCharacter", source = "entity", qualifiedByName = "characterIfRevealed")
    @Mapping(target = "handSize", expression = "java(entity.getHand() != null ? entity.getHand().size() : 0)")
    PublicPlayerResponse entityToPublicResponse(PlayerEntity entity);

    @Named("characterIfRevealed")
    default Integer characterIfRevealed(PlayerEntity player) {
        if (player == null || !player.getRevealed()) return null;
        return player.getCurrentCharacter();
    }
}
