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
@Table(name = "characters")
public class CharacterEntity {
    @Id
    private Long id;
    private Integer number;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private DistrictTypeEnum type;

    @ElementCollection
    @CollectionTable(name = "character_abilities", joinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "ability")
    @Enumerated(EnumType.STRING)
    private List<AbilityEnum> abilities;
}
