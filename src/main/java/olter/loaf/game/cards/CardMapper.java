package olter.loaf.game.cards;

import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CharacterResponse entityToListResponse(CharacterEntity entity);

    DistrictResponse entityToListResponse(DistrictEntity entity);
}
