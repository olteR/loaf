package olter.loaf.game.cards.model;

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
    private Integer cost;

    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum type;

    @Column(columnDefinition = "TEXT")
    private String cardText;
}
