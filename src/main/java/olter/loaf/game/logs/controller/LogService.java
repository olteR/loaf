package olter.loaf.game.logs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.cards.dto.AbilityTargetRequest;
import olter.loaf.game.cards.model.AbilityEnum;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.CharacterRepository;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.game.logs.dto.CharacterPickResponse;
import olter.loaf.game.logs.dto.PreviousGameResponse;
import olter.loaf.game.logs.dto.UserStatisticsResponse;
import olter.loaf.game.logs.model.LogEntity;
import olter.loaf.game.logs.model.LogRepository;
import olter.loaf.game.logs.model.LogTypeEnum;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.users.model.UserEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;
    private final PlayerRepository playerRepository;
    private final CharacterRepository characterRepository;

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

    public void logAbilityUse(GameEntity game, AbilityTargetRequest target, AbilityEnum source) throws
        IllegalAccessException {
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

    public UserStatisticsResponse composeStatistics(UserEntity user) {
        List<PlayerEntity> players = playerRepository.findAllEndedByUserId(user.getId());
        List<LogEntity> logs =
            logRepository.findAllByPlayerIdInAndGameEnded(players.stream().map(PlayerEntity::getId).collect(Collectors.toList()));
        UserStatisticsResponse response = new UserStatisticsResponse();

        // Generic stats
        response.setGamesPlayed(players.size());
        response.setGamesWon(players.stream().filter(player -> player.getResults().getPlacement().equals(1)).count());
        response.setAveragePlacement(
            players.stream().mapToInt(player -> player.getResults().getPlacement()).average().orElse(0));

        // Resource gathering stats
        response.setGoldChosen(logs.stream().filter(log -> log.getLogType() == LogTypeEnum.GATHER_RESOURCE &&
            Objects.equals(log.getTarget(), ResourceTypeEnum.GOLD.getValue())).count());
        response.setCardsChosen(logs.stream().filter(log -> log.getLogType() == LogTypeEnum.GATHER_RESOURCE &&
            Objects.equals(log.getTarget(), ResourceTypeEnum.CARDS.getValue())).count());

        // Character stats
        List<CharacterPickResponse> numberPicks = new ArrayList<>();
        for (Long i : LongStream.rangeClosed(1, 9).boxed().toList()) {
            CharacterPickResponse numberPick = new CharacterPickResponse();
            numberPick.setCharacter(i);
            numberPick.setPicks(logs.stream().filter(
                    log -> log.getLogType() == LogTypeEnum.SELECT_CHARACTER && Long.parseLong(log.getTarget()) % 9 == i)
                .count());
            numberPicks.add(numberPick);
        }
        response.setNumberPicks(numberPicks);

        List<CharacterPickResponse> characterPicks = new ArrayList<>();
        List<CharacterEntity> characters = characterRepository.findAll();
        characters.forEach(character -> {
            CharacterPickResponse characterPick = new CharacterPickResponse();
            characterPick.setCharacter(character.getId());
            characterPick.setPicks(logs.stream().filter(log -> log.getLogType() == LogTypeEnum.SELECT_CHARACTER &&
                Long.parseLong(log.getTarget()) == character.getId()).count());
            characterPicks.add(characterPick);
        });
        response.setCharacterPicks(characterPicks);

        response.setPreviousGames(players.stream().map(player -> {
            PreviousGameResponse previousGameResponse = new PreviousGameResponse();
            previousGameResponse.setCode(player.getGame().getLobby().getCode());
            previousGameResponse.setName(player.getGame().getLobby().getName());
            previousGameResponse.setPoints(player.getPoints());
            previousGameResponse.setPlacement(player.getResults().getPlacement());
            previousGameResponse.setPlayers(
                player.getGame().getPlayers().stream().map(PlayerEntity::getName).collect(Collectors.toList()));
            return previousGameResponse;
        }).toList());

        return response;
    }
}
