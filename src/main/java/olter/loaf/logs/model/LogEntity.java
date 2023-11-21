package olter.loaf.logs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import olter.loaf.common.BaseEntity;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "logs")
public class LogEntity extends BaseEntity {
    private Long lobbyId;
    private Long gameId;
    private Long playerId;
    private String source;
    private String target;
    private Long turn;

    @Enumerated(EnumType.STRING)
    private LogTypeEnum logType;
}
