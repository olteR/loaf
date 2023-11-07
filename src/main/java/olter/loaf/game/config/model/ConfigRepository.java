package olter.loaf.game.config.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {
    List<ConfigEntity> findAllByType(ConfigTypeEnum type);
}
