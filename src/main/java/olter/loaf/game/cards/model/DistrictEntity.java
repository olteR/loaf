package olter.loaf.game.cards.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "district_abilities", joinColumns = @JoinColumn(name = "district_id"))
    @Column(name = "ability")
    @Enumerated(EnumType.STRING)
    private List<AbilityEnum> abilities;

    public Boolean hasAbility(AbilityEnum ability) {
        return abilities.contains(ability);
    }
}
