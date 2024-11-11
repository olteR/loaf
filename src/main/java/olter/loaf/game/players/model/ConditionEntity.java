package olter.loaf.game.players.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.game.cards.model.ActivationEnum;
import olter.loaf.game.cards.model.ConditionDurationEnum;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "conditions")
public class ConditionEntity {
    @Id
    private Long id;

    private String name;
    private String icon;
    private Boolean global;
    private Boolean visible;

    @Enumerated(EnumType.STRING)
    private ActivationEnum activation;

    @Enumerated(EnumType.STRING)
    private ConditionDurationEnum duration;

    @Column(columnDefinition = "TEXT")
    private String description;
}
