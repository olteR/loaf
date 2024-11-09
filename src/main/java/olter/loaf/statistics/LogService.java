package olter.loaf.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.game.players.model.PlayerEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    public void logCharacterSelection(GameEntity game) {
        LogEntity log = new LogEntity();
        log.setPlayerId(game.getCurrentPlayer().getId());
        log.setTarget(String.valueOf(game.getCharacters().stream()
            .filter(c -> c.getNumber().equals(game.getCurrentPlayer().getCurrentCharacter())).findFirst()
            .orElseThrow(() -> new CorruptedGameException(game.getLobby().getCode())).getId()));
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
}
