package olter.loaf.game.players;

import olter.loaf.game.players.dto.ConditionResponse;
import olter.loaf.game.players.dto.PublicPlayerResponse;
import olter.loaf.game.players.model.ConditionEnum;
import olter.loaf.game.players.model.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "character", source = "entity", qualifiedByName = "characterIfRevealed")
    @Mapping(target = "conditions", qualifiedByName = "filteredConditions")
    @Mapping(target = "handSize", expression = "java(entity.getHand() != null ? entity.getHand().size() : 0)")
    PublicPlayerResponse entityToPublicResponse(PlayerEntity entity);

    @Named("characterIfRevealed")
    default Integer characterIfRevealed(PlayerEntity player) {
        if (player == null || !player.getRevealed()) return null;
        return player.getCharacterNumber();
    }

    @Named("filteredConditions")
    default List<ConditionResponse> filteredConditions(List<ConditionEnum> conditions) {
        return conditions.stream().filter(ConditionEnum::getVisible).map(this::conditionToResponse)
            .collect(Collectors.toList());
    }

    ConditionResponse conditionToResponse(ConditionEnum condition);
}
