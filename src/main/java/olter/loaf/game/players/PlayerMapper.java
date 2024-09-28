package olter.loaf.game.players;

import olter.loaf.game.games.dto.PublicPlayerResponse;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "handSize", expression = "java(entity.getHand() != null ? entity.getHand().size() : 0)")
    PublicPlayerResponse entityToPublicResponse(PlayerEntity entity);
}
