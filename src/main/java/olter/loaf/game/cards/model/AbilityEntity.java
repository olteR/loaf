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
@Table(name = "abilities")
public class AbilityEntity {
    @Id
    private Long id;

    private String name;
    private String icon;

    @Enumerated(EnumType.STRING)
    private AbilityActivationEnum type;

    @Enumerated(EnumType.STRING)
    private AbilityUsageEnum useRule;

    @Enumerated(EnumType.STRING)
    private AbilityTargetEnum target;

    @Column(columnDefinition = "TEXT")
    private String description;
}
