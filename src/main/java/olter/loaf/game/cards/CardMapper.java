package olter.loaf.game.cards;

import olter.loaf.game.cards.dto.AbilityResponse;
import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.AbilityEntity;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CharacterResponse entityToResponse(CharacterEntity entity);

    DistrictResponse entityToResponse(DistrictEntity entity);

    AbilityResponse entityToResponse(AbilityEntity ability);
}
