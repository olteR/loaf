package olter.loaf.game.districts;

import olter.loaf.game.districts.dto.DistrictListResponse;
import olter.loaf.game.districts.model.DistrictEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
  DistrictListResponse entityToListResponse(DistrictEntity entity);
}
