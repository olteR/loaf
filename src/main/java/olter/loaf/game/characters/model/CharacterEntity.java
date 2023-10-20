package olter.loaf.game.characters.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.districts.model.DistrictTypeEnum;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "characters")
public class CharacterEntity {
  @Id private Long id;
  private Integer number;
  private String name;

  @Enumerated(EnumType.STRING)
  private DistrictTypeEnum districtTypeBonus;
}
