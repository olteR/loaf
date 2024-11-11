package olter.loaf.game.cards.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.players.model.ConditionEntity;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "district_abilities", joinColumns = @JoinColumn(name = "ability_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<AbilityEntity> abilities;
}
