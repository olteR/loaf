package olter.loaf.game.characters;

import olter.loaf.game.characters.dto.CharacterListResponse;
import olter.loaf.game.characters.model.CharacterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CharacterMapper {
    CharacterListResponse entityToListResponse(CharacterEntity entity);
}
