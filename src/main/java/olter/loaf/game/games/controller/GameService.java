package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.config.model.ConfigEntity;
import olter.loaf.game.config.model.ConfigRepository;
import olter.loaf.game.config.model.ConfigTypeEnum;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.lobbies.model.LobbyEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameRepository gameRepository;
    private final ConfigRepository configRepository;
    public void createGameForLobby(LobbyEntity lobby) {
        GameEntity game = new GameEntity();
        game.setLobby(lobby);
        game.setUniqueCards(getDefaultUniqueDistricts());
        game.setCharacters(getDefaultCharacters());
        gameRepository.save(game);
    }

    private List<Long> getDefaultUniqueDistricts() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_UNIQUE_DISTRICT).stream().map(ConfigEntity::getConfigId).toList();
    }

    private List<Long> getDefaultCharacters() {
        return configRepository.findAllByType(ConfigTypeEnum.DEFAULT_CHARACTER).stream().map(ConfigEntity::getConfigId).toList();
    }
}
