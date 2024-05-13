package olter.loaf.game.logs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.games.exception.CorruptedGameException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.logs.model.LogEntity;
import olter.loaf.game.logs.model.LogRepository;
import olter.loaf.game.logs.model.LogTypeEnum;
import olter.loaf.game.players.model.PlayerEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    public void logCharacterSelection(GameEntity game, PlayerEntity player) {
        LogEntity log = new LogEntity();
        log.setGameId(game.getId());
        log.setUserId(player.getUserId());
        log.setTarget(String.valueOf(
            game.getCharacters().stream().filter(c -> c.getNumber().equals(player.getCurrentCharacter())).findFirst()
                .orElseThrow(() -> new CorruptedGameException(game.getId())).getId()));
        log.setTurn(game.getTurn());
        log.setLogType(LogTypeEnum.SELECT_CHARACTER);
        logRepository.save(log);
    }
}
