package olter.loaf.lobby.lobbies.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.game.games.controller.GameService;
import olter.loaf.lobby.lobbies.LobbyMapper;
import olter.loaf.lobby.lobbies.dto.*;
import olter.loaf.lobby.lobbies.exception.*;
import olter.loaf.lobby.lobbies.model.LobbyEntity;
import olter.loaf.lobby.lobbies.model.LobbyRepository;
import olter.loaf.lobby.lobbies.model.LobbyStatusEnum;
import olter.loaf.users.model.UserEntity;
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
    private final LobbyMapper lobbyMapper;
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PasswordEncoder passwordEncoder;

    public LobbyDetailsResponse getLobby(String code, UserEntity loggedInUser) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        log.info("Getting lobby " + lobby.getName() + " for " + loggedInUser.getName());
        if (!lobby.getMembers().contains(loggedInUser)) {
            throw new NotInLobbyException(lobby.getId(), loggedInUser.getId());
        }
        return lobbyMapper.entityToDetailsResponse(lobby);
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
        lobby.setGame(gameService.createGameForLobby(lobby.getCode()));
        lobbyRepository.save(lobby);

        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public LobbyDetailsResponse joinLobby(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        List<UserEntity> members = lobby.getMembers();
        log.info(user.getName() + " joining lobby " + lobby.getName() + "...");

        if (members.contains(user)) {
            throw new AlreadyJoinedException(lobby.getId(), user.getId());
        }
        if (lobby.getMaxMembers() == members.size()) {
            throw new LobbyFullException(lobby.getId());
        }
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getId());
        }

        lobby
            .getMembers()
            .forEach(
                m -> {
                    log.info("Broadcasting join to " + m.getName());
                    simpMessagingTemplate.convertAndSendToUser(
                        m.getName(),
                        "/topic/lobby/update",
                        new LobbyUpdateDto(LobbyUpdateTypeEnum.JOIN, user));
                });

        members.add(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);
        return lobbyMapper.entityToDetailsResponse(lobby);
    }

    public void leaveLobby(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
        List<UserEntity> members = lobby.getMembers();
        log.info(user.getName() + " leaving lobby " + lobby.getName() + "...");

        if (!members.contains(user)) {
            throw new NotInLobbyException(lobby.getId(), user.getId());
        }
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getId());
        }

        members.remove(user);
        lobby.setMembers(members);
        lobbyRepository.save(lobby);
        lobby
            .getMembers()
            .forEach(
                m -> {
                    log.info("Broadcasting leave to " + m.getName());
                    simpMessagingTemplate.convertAndSendToUser(
                        m.getName(),
                        "/topic/lobby/update",
                        new LobbyUpdateDto(LobbyUpdateTypeEnum.LEAVE, user.getId()));
                });
    }

    public void kickMember(UserEntity user, LobbyMemberInteractionDto req) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(req.getCode())
                .orElseThrow(
                    () -> new ResourceNotFoundException(LobbyEntity.class.getName(), req.getCode()));
        List<UserEntity> members = lobby.getMembers();

        if (!Objects.equals(lobby.getOwner(), user.getId())) {
            throw new NoPrivilegeException(lobby.getId(), user.getId());
        }
        if (!members.stream().map(UserEntity::getId).toList().contains(req.getMemberId())) {
            throw new NotInLobbyException(lobby.getId(), user.getId());
        }
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getId());
        }

        members.forEach(
            m -> {
                log.info("Broadcasting kick to " + m.getName());
                simpMessagingTemplate.convertAndSendToUser(
                    m.getName(),
                    "/topic/lobby/update",
                    new LobbyUpdateDto(LobbyUpdateTypeEnum.KICK, req.getMemberId()));
            });
        members.removeIf(m -> Objects.equals(m.getId(), req.getMemberId()));
        lobby.setMembers(members);
        lobbyRepository.save(lobby);
    }

    public void promoteMember(UserEntity user, LobbyMemberInteractionDto req) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(req.getCode())
                .orElseThrow(
                    () -> new ResourceNotFoundException(LobbyEntity.class.getName(), req.getCode()));

        if (!Objects.equals(lobby.getOwner(), user.getId())) {
            throw new NoPrivilegeException(lobby.getId(), user.getId());
        }
        if (!lobby.getMembers().stream().map(UserEntity::getId).toList().contains(req.getMemberId())) {
            throw new NotInLobbyException(lobby.getId(), user.getId());
        }
        if (!Objects.equals(lobby.getStatus(), LobbyStatusEnum.CREATED)) {
            throw new GameInProgressException(lobby.getId());
        }

        lobby
            .getMembers()
            .forEach(
                m -> {
                    log.info("Broadcasting promotion to " + m.getName());
                    simpMessagingTemplate.convertAndSendToUser(
                        m.getName(),
                        "/topic/lobby/update",
                        new LobbyUpdateDto(LobbyUpdateTypeEnum.OWNER, req.getMemberId()));
                });
        lobby.setOwner(req.getMemberId());
        lobbyRepository.save(lobby);
    }

    public void startGame(String code, UserEntity user) {
        LobbyEntity lobby =
            lobbyRepository
                .findFirstByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));

        if (!Objects.equals(lobby.getOwner(), user.getId())) {
            throw new NoPrivilegeException(lobby.getId(), user.getId());
        }

        gameService.startGame(lobby);
        lobby.setStatus(LobbyStatusEnum.ONGOING);

        lobby
            .getMembers()
            .forEach(
                m -> {
                    log.info("Broadcasting start to " + m.getName());
                    simpMessagingTemplate.convertAndSendToUser(
                        m.getName(),
                        "/topic/lobby/update",
                        new LobbyUpdateDto(LobbyUpdateTypeEnum.START, null));
                });

        lobbyRepository.save(lobby);
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
