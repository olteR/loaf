package olter.loaf.game.districts.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.districts.model.DistrictTypeEnum;

@Getter
@Setter
@RequiredArgsConstructor
public class DistrictListResponse {
  private Long id;
  private String name;
  private DistrictTypeEnum type;
  private Integer cost;
}
