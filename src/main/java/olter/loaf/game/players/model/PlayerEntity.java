package olter.loaf.game.players.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.games.exception.InvalidTransactionException;
import olter.loaf.game.games.model.GameEntity;
import org.hibernate.annotations.Formula;

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

    @Formula("(SELECT u.name FROM users u WHERE u.id = user_id)")
    private String name;

    @Formula("(SELECT u.display_name FROM users u WHERE u.id = user_id)")
    private String displayName;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "player_hand",
        joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> hand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "player_draw",
        joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> drawnCards;

    public void giveGold(Integer gold) {
        this.gold += gold;
    }

    public void takeGold(Integer gold) {
        if (gold > this.gold) {
            throw new InvalidTransactionException(this.getId());
        }
        this.gold -= gold;
    }

    public void giveCards(List<DistrictEntity> cards) {
        this.hand.addAll(cards);
    }
}
