package olter.loaf.lobby.lobbies.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.games.controller.GameService;
import olter.loaf.game.players.model.PlayerEntity;
import olter.loaf.game.players.model.PlayerRepository;
import olter.loaf.lobby.lobbies.LobbyMapper;
import olter.loaf.lobby.lobbies.dto.*;
import olter.loaf.lobby.lobbies.exception.*;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.lobby.lobbies.model.LobbyRepository;
import olter.loaf.lobby.lobbies.model.LobbyStatusEnum;
import olter.loaf.users.UserMapper;
import olter.loaf.users.model.UserEntity;
import olter.loaf.users.model.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {
    private final LobbyRepository lobbyRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final LobbyMapper lobbyMapper;
    private final UserMapper userMapper;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PasswordEncoder passwordEncoder;

    public LobbyDetailsResponse getLobby(String code, UserEntity loggedInUser) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        log.info("Getting lobby " + lobby.getName() + " for " + loggedInUser.getName());
        validateContainment(lobby, loggedInUser.getId());
        LobbyDetailsResponse response = lobbyMapper.entityToDetailsResponse(lobby);
        response.setMembers(userRepository.getLobbyMembers(lobby.getCode()));
        return response;
    }

    public List<LobbyListResponse> getLobbies(UserEntity loggedInUser) {
        log.info("Getting lobbies for " + loggedInUser.getName());
        return lobbyRepository.findAll().stream()
            .filter(l -> !l.getMembers().contains(loggedInUser))
            .map(lobbyMapper::entityToListResponse)
            .toList();
    }

    public List<LobbyListResponse> getMyGames(UserEntity loggedInUser) {
        log.info("Getting games for " + loggedInUser.getName());
        return lobbyRepository.findAll().stream()
            .filter(l -> l.getMembers().contains(loggedInUser))
            .map(lobbyMapper::entityToListResponse)
            .toList();
    }

    public LobbyDetailsResponse createLobby(LobbyCreationRequest request, UserEntity creator) {
        log.info(request.getName() + " creating lobby...");
        LobbyEntity lobby = new LobbyEntity();
        lobbyMapper.map(request, lobby);
        lobby.setOwner(creator.getId());
        lobby.setMembers(List.of(creator));
        lobby.setStatus(LobbyStatusEnum.CREATED);

        while (lobby.getCode() == null || lobbyRepository.existsByCode(lobby.getCode())) {
            lobby.setCode(generateLobbyCode());
            log.info(request.getName() + "Lobby code generated: " + lobby.getCode());
        }

        if (request.getSecured() != null && request.getSecured()) {
            lobby.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        lobby.setGame(gameService.createGameForLobby(lobby));
        lobbyRepository.save(lobby);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public LobbyDetailsResponse joinLobby(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        List<UserEntity> members = lobby.getMembers();
        log.info(user.getName() + " joining lobby " + lobby.getName());

        validateJoin(lobby, user);
        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.JOIN, userMapper.entityToResponse(user));

        members.add(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);

        PlayerEntity p = new PlayerEntity();
        p.setUserId(user.getId());
        p.setOrder(lobby.getMembers().size());
        p.setGame(lobby.getGame());
        playerRepository.save(p);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public void leaveLobby(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        List<UserEntity> members = lobby.getMembers();
        PlayerEntity leavingPlayer = playerRepository.findByUserIdAndGame(user.getId(), lobby.getGame())
            .orElseThrow(() -> new NotInLobbyException(lobby.getId(), user.getId()));
        log.info(user.getName() + " leaving lobby " + lobby.getName());

        validateProgress(lobby);

        members.remove(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);

        playerRepository.saveAll(
            playerRepository.findAllByGameAndOrderGreaterThan(lobby.getGame(), leavingPlayer.getOrder()).stream()
                .peek(p -> p.setOrder(p.getOrder() - 1)).toList());
        playerRepository.delete(leavingPlayer);

        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.LEAVE, user.getId());
    }

    public void kickMember(UserEntity user, LobbyMemberInteractionDto req) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(req.getCode())
                .orElseThrow(
                    () -> new ResourceNotFoundException(LobbyEntity.class.getName(), req.getCode()));
        List<UserEntity> members = lobby.getMembers();
        PlayerEntity kickedPlayer = playerRepository.findByUserIdAndGame(req.getMemberId(), lobby.getGame())
            .orElseThrow(() -> new NotInLobbyException(lobby.getId(), req.getMemberId()));
        log.info("Kicking member " + req.getMemberId() + " from lobby " + req.getCode());

        validateOwnerRequest(lobby, user);

        playerRepository.saveAll(
            playerRepository.findAllByGameAndOrderGreaterThan(lobby.getGame(), kickedPlayer.getOrder()).stream()
                .peek(p -> p.setOrder(p.getOrder() - 1)).toList());
        playerRepository.delete(kickedPlayer);
        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.KICK, req.getMemberId());

        members.removeIf(m -> Objects.equals(m.getId(), req.getMemberId()));
        lobby.setMembers(members);
        lobbyRepository.save(lobby);
    }

    public void promoteMember(UserEntity user, LobbyMemberInteractionDto req) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(req.getCode())
                .orElseThrow(
                    () -> new ResourceNotFoundException(LobbyEntity.class.getName(), req.getCode()));
        log.info("Promoting member " + req.getMemberId() + " in lobby " + req.getCode());

        validateOwnerRequest(lobby, user);
        validateContainment(lobby, req.getMemberId());
        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.OWNER, req.getMemberId());

        lobby.setOwner(req.getMemberId());
        lobbyRepository.save(lobby);
    }

    public void deleteLobby(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(code)
                .orElseThrow(
                    () -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        log.info("Deleting lobby " + code);

        validateOwnerRequest(lobby, user);
        lobbyRepository.delete(lobby);
        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.DELETE, null);
    }

    public void startGame(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));

        validateOwnerRequest(lobby, user);
        gameService.startGame(lobby);
        lobby.setStatus(LobbyStatusEnum.ONGOING);
        broadcastOnWebsocket(lobby, LobbyUpdateTypeEnum.START, null);

        lobbyRepository.save(lobby);
    }

    private void broadcastOnWebsocket(LobbyEntity lobby, LobbyUpdateTypeEnum updateType, Object change) {
        lobby
            .getMembers()
            .forEach(
                m -> {
                    log.info("Broadcasting " + updateType.getValue() + " to " + m.getId());
                    simpMessagingTemplate.convertAndSendToUser(String.valueOf(m.getId()), "/topic/lobby/update",
                        new LobbyUpdateDto(updateType, change));
                });
    }

    private void validateOwnerRequest(LobbyEntity lobby, UserEntity user) {
        if (!Objects.equals(lobby.getOwner(), user.getId())) {
            throw new NoPrivilegeException(lobby.getId(), user.getId());
        }
        validateProgress(lobby);
    }

    private void validateContainment(LobbyEntity lobby, Long userId) {
        if (!lobby.getMembers().stream().map(UserEntity::getId).toList().contains(userId)) {
            throw new NotInLobbyException(lobby.getId(), userId);
        }
    }

    private void validateJoin(LobbyEntity lobby, UserEntity user) {
        if (lobby.getMembers().contains(user)) {
            throw new AlreadyJoinedException(lobby.getId(), user.getId());
        }
        if (lobby.getMaxMembers() == lobby.getMembers().size()) {
            throw new LobbyFullException(lobby.getId());
        }
        validateProgress(lobby);
    }

    private void validateProgress(LobbyEntity lobby) {
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getId());
        }
    }

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
