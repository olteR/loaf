package olter.loaf.statistics;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.cards.dto.AbilityTargetRequest;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.ResourceTypeEnum;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    public void logCharacterSelection(GameEntity game) {
        LogEntity log = new LogEntity();
        log.setPlayerId(game.getCurrentPlayer().getId());
        log.setTarget(String.valueOf(game.getCurrentPlayer().getCharacter().getId()));
        log.setTurn(game.getTurn());
        log.setLogType(LogTypeEnum.SELECT_CHARACTER);
        logRepository.save(log);
    }

    public void logResourceGathering(GameEntity game, ResourceTypeEnum resource) {
        LogEntity log = new LogEntity();
        log.setPlayerId(game.getCurrentPlayer().getId());
        log.setTarget(resource.getValue());
        log.setTurn(game.getTurn());
        log.setLogType(LogTypeEnum.GATHER_RESOURCE);
        logRepository.save(log);
    }

    public void logDistrictBuilding(GameEntity game, Long districtId) {
        LogEntity log = new LogEntity();
        log.setPlayerId(game.getCurrentPlayer().getId());
        log.setTarget(String.valueOf(districtId));
        log.setTurn(game.getTurn());
        log.setLogType(LogTypeEnum.BUILD);
        logRepository.save(log);
    }

    public void logAbilityUse(GameEntity game, AbilityTargetRequest target, AbilityEnum source) throws IllegalAccessException {
        List<LogEntity> logs = new ArrayList<>();
        if (target != null) {
            for (Field field : target.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(target);
                if (value != null) {
                    LogEntity log = new LogEntity();
                    log.setPlayerId(game.getCurrentPlayer().getId());
                    log.setSource(source.getValue());
                    log.setTarget(String.valueOf(value));
                    log.setTurn(game.getTurn());
                    log.setLogType(LogTypeEnum.ABILITY_USE);
                    logs.add(log);
                }
            }
        } else {
            LogEntity log = new LogEntity();
            log.setPlayerId(game.getCurrentPlayer().getId());
            log.setSource(source.getValue());
            log.setTurn(game.getTurn());
            log.setLogType(LogTypeEnum.ABILITY_USE);
            logs.add(log);
        }
        logRepository.saveAll(logs);
    }
}
