package olter.loaf.game.districts.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
public class DistrictEntity {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum type;

    private Integer cost;
}
