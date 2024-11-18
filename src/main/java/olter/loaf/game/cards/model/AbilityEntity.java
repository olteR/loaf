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
@Table(name = "abilities")
public class AbilityEntity {
    @Id
    private Long id;

    @ElementCollection
    @CollectionTable(name = "ability_icons", joinColumns = @JoinColumn(name = "ability_id"))
    @Column(name = "icon")
    private List<String> icons;

    @Enumerated(EnumType.STRING)
    private ActivationEnum type;

    @Enumerated(EnumType.STRING)
    private AbilityUsageEnum useRule;

    @Enumerated(EnumType.STRING)
    private AbilityTargetEnum target;

    @Column(columnDefinition = "TEXT")
    private String description;
}
