package olter.loaf.lobby.lobbies.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.users.model.UserEntity;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "lobbies")
public class LobbyEntity extends BaseEntity {
    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String code;

    private Boolean secured;
    private Integer maxMembers;

    private Long owner;
    private String password;

    @Enumerated(EnumType.STRING)
    private LobbyStatusEnum status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private GameEntity game;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "lobby_users",
        joinColumns = {@JoinColumn(name = "lobby_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<UserEntity> members;
}
