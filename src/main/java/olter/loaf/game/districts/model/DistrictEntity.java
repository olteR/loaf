package olter.loaf.game.districts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
public class DistrictEntity {
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    private String cardName;

    @Column(columnDefinition = "TEXT")
    private String cardText;

    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum type;

    private Integer cost;
}
