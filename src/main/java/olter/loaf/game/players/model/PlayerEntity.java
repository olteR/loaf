package olter.loaf.game.players.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.games.model.GameEntity;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "players")
public class PlayerEntity extends BaseEntity {
    private Long userId;
    private Integer gold;
    private Integer currentCharacter;
    private Boolean isRevealed;

    @Column(name = "player_order")
    private Integer order;

    @ElementCollection
    @CollectionTable(name = "player_hand", joinColumns = @JoinColumn(name = "player_id"))
    private List<Long> hand;

    @ElementCollection
    @CollectionTable(name = "player_unavailable_characters", joinColumns = @JoinColumn(name = "player_id"))
    private List<Integer> unavailableCharacters;

    @ElementCollection
    @CollectionTable(name = "player_skipped_characters", joinColumns = @JoinColumn(name = "player_id"))
    private List<Integer> skippedCharacters;

    @ElementCollection
    @CollectionTable(name = "player_districts", joinColumns = @JoinColumn(name = "player_id"))
    private List<DistrictEmbeddable> districts;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;
}
