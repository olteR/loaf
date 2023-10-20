package olter.loaf.game.games.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.districts.model.DistrictEntity;
import olter.loaf.game.players.model.PlayerEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "games")
public class GameEntity extends BaseEntity {
  private Long lobbyId;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "game_deck",
      joinColumns = {@JoinColumn(name = "game_id")},
      inverseJoinColumns = {@JoinColumn(name = "district_id")})
  private List<DistrictEntity> deck;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
  private List<PlayerEntity> players;
}
