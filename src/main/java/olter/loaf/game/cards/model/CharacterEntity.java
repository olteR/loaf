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
@Table(name = "characters")
public class CharacterEntity {
    @Id
    private Long id;
    private Integer number;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String cardText;

    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum districtTypeBonus;
}
