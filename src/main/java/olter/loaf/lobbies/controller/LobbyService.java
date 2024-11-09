package olter.loaf.lobbies.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.cards.model.CharacterEntity;
import olter.loaf.game.cards.model.CharacterRepository;
import olter.loaf.game.games.controller.GameService;
import olter.loaf.game.games.exception.NotInGameException;
import olter.loaf.game.games.model.GameEntity;
import olter.loaf.game.games.model.GameRepository;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobbies.LobbyMapper;
import olter.loaf.lobbies.dto.*;
import olter.loaf.lobbies.exception.*;
import olter.loaf.lobbies.model.LobbyEntity;
import olter.loaf.lobbies.model.LobbyRepository;
import olter.loaf.lobbies.model.LobbyStatusEnum;
import olter.loaf.users.UserMapper;
import olter.loaf.users.model.UserEntity;
import olter.loaf.users.model.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {
    private final LobbyRepository lobbyRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final CharacterRepository characterRepository;
    private final LobbyMapper lobbyMapper;
    private final UserMapper userMapper;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${loaf.config.unique-districts}")
    private Integer UNIQUE_DISTRICTS;

    public LobbyDetailsResponse getLobby(String code, UserEntity loggedInUser) {
        LobbyEntity lobby = findLobby(code);
        log.info("Getting lobby {} for {}", lobby.getName(), loggedInUser.getName());
        validateContainment(lobby, loggedInUser.getId());
        LobbyDetailsResponse response = lobbyMapper.entityToDetailsResponse(lobby);
        response.setMembers(userRepository.getLobbyMembers(lobby.getCode()));
        return response;
    }

    public List<LobbyListResponse> getLobbies(UserEntity loggedInUser) {
        log.info("Getting lobbies for {}", loggedInUser.getName());
        return lobbyRepository.findAll().stream().filter(l -> !l.getMembers().contains(loggedInUser))
            .map(lobbyMapper::entityToListResponse).toList();
    }

    public List<LobbyListResponse> getMyGames(UserEntity loggedInUser) {
        log.info("Getting games for {}", loggedInUser.getName());
        return lobbyRepository.findAll().stream().filter(l -> l.getMembers().contains(loggedInUser))
            .map(lobbyMapper::entityToListResponse).toList();
    }

    public LobbyDetailsResponse createLobby(LobbyRequest request, UserEntity creator) {
        log.info("{} creating lobby...", request.getName());
        LobbyEntity lobby = new LobbyEntity();
        lobbyMapper.map(request, lobby);
        lobby.setOwner(creator.getId());
        lobby.setMembers(List.of(creator));
        lobby.setStatus(LobbyStatusEnum.CREATED);

        while (lobby.getCode() == null || lobbyRepository.existsByCode(lobby.getCode())) {
            lobby.setCode(generateLobbyCode());
            log.info("{} Lobby code generated: {}", request.getName(), lobby.getCode());
        }

        if (request.getSecured() != null && request.getSecured()) {
            lobby.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        lobby.setGame(gameService.createGameForLobby(lobby));
        lobbyRepository.save(lobby);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public LobbyDetailsResponse editLobby(LobbyRequest request, String code, UserEntity user) {
        log.info("{} editing lobby {}...", request.getName(), code);
        LobbyEntity lobby = findLobby(code);
        validateOwnerRequest(lobby, user);
        if (lobby.getMembers().size() > request.getMaxMembers()) {
            throw new TooManyMembersException(code);
        }

        lobbyMapper.map(request, lobby);
        if (request.getSecured() != null && request.getSecured()) {
            lobby.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        lobbyRepository.save(lobby);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public LobbyDetailsResponse joinLobby(String code, UserEntity user) {
        LobbyEntity lobby = findLobby(code);
        List<UserEntity> members = lobby.getMembers();
        log.info("{} joining lobby {}", user.getName(), lobby.getName());

        validateJoin(lobby, user);
        broadcastOnWebsocket(members, LobbyUpdateTypeEnum.JOIN, userMapper.entityToResponse(user));

        members.add(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);

        PlayerEntity p = new PlayerEntity();
        p.setUserId(user.getId());
        p.setOrder(lobby.getMembers().size());
        p.setGame(lobby.getGame());
        p.setIsRevealed(false);
        playerRepository.save(p);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public void leaveLobby(String code, UserEntity user) {
        LobbyEntity lobby = findLobby(code);
        List<UserEntity> members = lobby.getMembers();
        PlayerEntity leavingPlayer = playerRepository.findByUserIdAndGame(user.getId(), lobby.getGame())
            .orElseThrow(() -> new NotInLobbyException(code, user.getId()));
        log.info("{} leaving lobby {}", user.getName(), lobby.getName());

        validateProgress(lobby);

        members.remove(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);

        playerRepository.saveAll(
            playerRepository.findAllByGameAndOrderGreaterThan(lobby.getGame(), leavingPlayer.getOrder()).stream()
                .peek(p -> p.setOrder(p.getOrder() - 1)).toList());
        playerRepository.delete(leavingPlayer);

        broadcastOnWebsocket(members, LobbyUpdateTypeEnum.LEAVE, user.getId());
    }

    public void kickMember(UserEntity user, LobbyMemberInteractionDto request) {
        LobbyEntity lobby = findLobby(request.getCode());
        List<UserEntity> members = lobby.getMembers();

        PlayerEntity kickedPlayer = playerRepository.findByUserIdAndGame(request.getMemberId(), lobby.getGame())
            .orElseThrow(() -> new NotInLobbyException(request.getCode(), request.getMemberId()));
        log.info("Kicking member {} from lobby {}", request.getMemberId(), request.getCode());

        validateOwnerRequest(lobby, user);

        playerRepository.saveAll(
            playerRepository.findAllByGameAndOrderGreaterThan(lobby.getGame(), kickedPlayer.getOrder()).stream()
                .peek(p -> p.setOrder(p.getOrder() - 1)).toList());
        playerRepository.delete(kickedPlayer);
        broadcastOnWebsocket(members, LobbyUpdateTypeEnum.KICK, request.getMemberId());

        members.removeIf(m -> Objects.equals(m.getId(), request.getMemberId()));
        lobby.setMembers(members);
        lobbyRepository.save(lobby);
    }

    public void promoteMember(UserEntity user, LobbyMemberInteractionDto request) {
        LobbyEntity lobby = findLobby(request.getCode());
        log.info("Promoting member {} in lobby {}", request.getMemberId(), request.getCode());

        validateOwnerRequest(lobby, user);
        validateContainment(lobby, request.getMemberId());
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.OWNER, request.getMemberId());

        lobby.setOwner(request.getMemberId());
        lobbyRepository.save(lobby);
    }

    public void reorderLobby(List<LobbyMemberDto> request, String code, UserEntity user) {
        LobbyEntity lobby = findLobby(code);
        log.info("Reordering lobby {}", code);
    }

    public void deleteLobby(String code, UserEntity user) {
        LobbyEntity lobby = findLobby(code);
        log.info("Deleting lobby {}", code);

        validateOwnerRequest(lobby, user);
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.DELETE, null);
        lobbyRepository.delete(lobby);
    }

    public void updateCharacters(UserEntity user, LobbySettingDto request) {
        LobbyEntity lobby = findLobby(request.getCode());
        log.info("Updating characters in lobby {}", request.getCode());
        validateOwnerRequest(lobby, user);

        List<CharacterEntity> characters = characterRepository.findAllByIdIn(request.getIds());
        characters.sort(Comparator.comparing(CharacterEntity::getNumber));
        for (int i = 0; i < characters.size(); i++) {
            if (!characters.get(i).getNumber().equals(i + 1)) {
                throw new InvalidCharactersException(request.getCode());
            }
        }

        GameEntity game = lobby.getGame();
        game.setCharacters(characters);
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.CHARACTERS, request.getIds());
        gameRepository.save(game);
    }

    public void updateDistricts(UserEntity user, LobbySettingDto request) {
        LobbyEntity lobby = findLobby(request.getCode());
        log.info("Updating districts in lobby {}", request.getCode());
        validateOwnerRequest(lobby, user);

        if (new HashSet<>(request.getIds()).size() != UNIQUE_DISTRICTS) {
            throw new InvalidDistrictsException(request.getCode());
        }
        GameEntity game = lobby.getGame();
        game.setUniqueDistricts(request.getIds());
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.DISTRICTS, request.getIds());
        gameRepository.save(game);
    }

    public void crownMember(UserEntity user, LobbyMemberInteractionDto request) {
        LobbyEntity lobby = findLobby(request.getCode());
        log.info("Changing crowned user to {} in lobby {}", request.getMemberId(), request.getCode());
        validateOwnerRequest(lobby, user);

        GameEntity game = lobby.getGame();
        if (request.getMemberId() == null) {
            game.setCrownedPlayer(null);
        } else {
            game.setCrownedPlayer(
                game.getPlayers().stream().filter(player -> Objects.equals(player.getUserId(), request.getMemberId()))
                    .findFirst().orElseThrow(() -> new NotInGameException(request.getCode(), request.getMemberId())));
        }
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.CROWN, request.getMemberId());
        gameRepository.save(game);
    }

    public void startGame(String code, UserEntity user) {
        LobbyEntity lobby = findLobby(code);
        validateOwnerRequest(lobby, user);

        gameService.startGame(lobby);
        lobby.setStatus(LobbyStatusEnum.ONGOING);
        broadcastOnWebsocket(lobby.getMembers(), LobbyUpdateTypeEnum.START, null);

        lobbyRepository.save(lobby);
    }

    // Returns the lobby or throws an exception if it doesn't exist
    private LobbyEntity findLobby(String code) {
        return lobbyRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
    }

    // Broadcasts the update to all the members of the lobby
    private void broadcastOnWebsocket(List<UserEntity> members, LobbyUpdateTypeEnum updateType, Object change) {
        members.forEach(m -> {
            log.info("Broadcasting {} to {}", updateType.getValue(), m.getId());
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(m.getId()), "/topic/lobby/update",
                new LobbyUpdateDto(updateType, change));
        });
    }

    // Validates if the given user is the owner and the game hasn't started yet
    private void validateOwnerRequest(LobbyEntity lobby, UserEntity user) {
        if (!Objects.equals(lobby.getOwner(), user.getId())) {
            throw new NoPrivilegeException(lobby.getCode(), user.getId());
        }
        validateProgress(lobby);
    }

    // Validates if the given user in the lobby
    private void validateContainment(LobbyEntity lobby, Long userId) {
        if (!lobby.getMembers().stream().map(UserEntity::getId).toList().contains(userId)) {
            throw new NotInLobbyException(lobby.getCode(), userId);
        }
    }

    // Validates if the given user can join the lobby
    private void validateJoin(LobbyEntity lobby, UserEntity user) {
        if (lobby.getMembers().contains(user)) {
            throw new AlreadyJoinedException(lobby.getCode(), user.getId());
        }
        if (lobby.getMaxMembers() == lobby.getMembers().size()) {
            throw new TooManyMembersException(lobby.getCode());
        }
        validateProgress(lobby);
    }

    // Validates that the game hasn't started yet
    private void validateProgress(LobbyEntity lobby) {
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getCode());
        }
    }

    // Generates an 8 character long alphanumeric string
    private String generateLobbyCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
