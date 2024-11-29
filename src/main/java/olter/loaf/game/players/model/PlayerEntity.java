package olter.loaf.game.players.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.DistrictEntity;
import olter.loaf.game.games.model.GameEntity;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "players")
public class PlayerEntity extends BaseEntity {
    private Long userId;
    private Integer gold;
    private Integer buildLimit;
    private Boolean revealed;
    private Long abilityTarget;

    @Column(name = "character_number")
    private Integer character;

    @Column(name = "player_order")
    private Integer order;

    @Formula("(SELECT u.name FROM users u WHERE u.id = user_id)")
    private String name;

    @Enumerated(EnumType.STRING)
    private AbilityEnum usingAbility;

    @Embedded
    private PlayerResults results;

    @ElementCollection
    @CollectionTable(name = "player_conditions", joinColumns = @JoinColumn(name = "player_id"))
    @Enumerated(EnumType.STRING)
    private Set<ConditionEnum> conditions = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "player_used_abilities", joinColumns = @JoinColumn(name = "player_id"))
    @Enumerated(EnumType.STRING)
    private Set<AbilityEnum> usedAbilities = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "player_unavailable_characters", joinColumns = @JoinColumn(name = "player_id"))
    private List<Integer> unavailableCharacters;

    @ElementCollection
    @CollectionTable(name = "player_skipped_characters", joinColumns = @JoinColumn(name = "player_id"))
    private List<Integer> skippedCharacters;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "player_districts", joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> districts;

    @ManyToOne(fetch = FetchType.LAZY)
    private GameEntity game;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "player_hand", joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> hand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "player_draw", joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "district_id"))
    private List<DistrictEntity> drawnCards;

    public void giveGold(Integer amount) {
        this.gold += amount;
    }

    public void giveDistrictPoints(Integer amount) {
        this.results.giveDistrictPoints(amount);
    }

    public void giveBonusPoints(Integer amount) {
        this.results.giveBonusPoints(amount);
    }

    public Integer getPoints() {
        if (this.results == null) {
            return 0;
        }
        return this.results.getTotalPoints();
    }

    public void giveDistrict(DistrictEntity district) {
        this.districts.add(district);
        this.giveDistrictPoints(district.getCost());
        if (this.districts.size() == (this.hasDistrictAbility(AbilityEnum.MONUMENT) ? 6 : 7)) {
            this.results.setFinished(true);
            if (!game.getIsFinalTurn()) {
                this.results.setFinishedFirst(true);
                game.setIsFinalTurn(true);
            }
        }
    }

    public DistrictEntity removeDistrict(int index) {
        DistrictEntity removed = this.districts.remove(index);
        this.results.takeDistrictPoints(removed.getCost());
        return removed;
    }

    public DistrictEntity removeDistrict(DistrictEntity district) {
        this.results.takeDistrictPoints(district.getCost());
        this.districts.remove(district);
        return district;
    }

    public Integer takeGold(Integer gold) {
        Integer taken = gold;
        if (gold > this.gold) {
            taken = this.gold;
            this.gold = 0;
        }
        this.gold -= gold;
        return taken;
    }

    public void giveCard(DistrictEntity card) {
        this.hand.add(card);
    }

    public void giveCards(List<DistrictEntity> cards) {
        this.hand.addAll(cards);
    }

    public DistrictEntity takeCard(int index) {
        return this.hand.remove(index);
    }

    public void takeCard(DistrictEntity card) {
        this.hand.remove(card);
    }

    public DistrictEntity takeRandomCard() {
        Random r = new Random();
        return this.hand.remove(r.nextInt(this.hand.size()));
    }

    public CharacterEntity getCharacter() {
        return this.game.getCharacters().stream().filter(character -> character.getNumber().equals(this.character))
            .findFirst().orElse(null);
    }

    public Integer getCharacterNumber() {
        return this.character;
    }

    public void giveCondition(ConditionEnum condition) {
        this.conditions.add(condition);
    }

    public void removeCondition(ConditionEnum condition) {
        this.conditions.remove(condition);
    }

    public Boolean hasCondition(ConditionEnum condition) {
        return this.conditions.contains(condition);
    }

    public Boolean hasDistrictAbility(AbilityEnum ability) {
        return this.districts.stream().flatMap(district -> district.getAbilities().stream()).toList().contains(ability);
    }
}
