package olter.loaf.game.config.model;

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
@Table(name = "config")
public class ConfigEntity {
    @Id
    private Long id;

    private Long configId;
    private Integer configValue;

    @Enumerated(EnumType.STRING)
    private ConfigTypeEnum type;
}
