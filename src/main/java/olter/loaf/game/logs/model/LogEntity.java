package olter.loaf.game.logs.model;

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
    private Long gameId;
    private Long userId;
    private String source;
    private String target;
    private Integer turn;

    @Enumerated(EnumType.STRING)
    private LogTypeEnum logType;
}
