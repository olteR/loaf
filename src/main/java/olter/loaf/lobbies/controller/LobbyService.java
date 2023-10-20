package olter.loaf.lobbies.controller;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.lobbies.LobbyMapper;
import olter.loaf.lobbies.dto.*;
import olter.loaf.lobbies.exception.AlreadyJoinedException;
import olter.loaf.lobbies.exception.LobbyFullException;
import olter.loaf.lobbies.exception.NoPrivilegeException;
import olter.loaf.lobbies.exception.NotInLobbyException;
import olter.loaf.lobbies.model.LobbyEntity;
import olter.loaf.lobbies.model.LobbyRepository;
import olter.loaf.users.model.UserEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {
  private final LobbyRepository lobbyRepository;
  private final LobbyMapper lobbyMapper;
  private final LobbyWebsocketController lobbyWebsocketController;
  private final SimpMessagingTemplate simpMessagingTemplate;

  private final PasswordEncoder passwordEncoder;

  public List<LobbyListResponse> getMyGames(UserEntity loggedInUser) {
    log.info("Getting games for " + loggedInUser.getName());
    return lobbyRepository.findAll().stream()
        .filter(l -> l.getMembers().contains(loggedInUser))
        .map(lobbyMapper::entityToListResponse)
        .toList();
  }

  public LobbyDetailsResponse getLobby(String code, UserEntity loggedInUser) {
    LobbyEntity lobby =
        lobbyRepository
            .findFirstByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
    log.info("Getting lobby" + lobby.getName() + " for " + loggedInUser.getName());
    if (!lobby.getMembers().contains(loggedInUser)) {
      throw new NotInLobbyException(lobby.getId(), loggedInUser.getId());
    }
    return lobbyMapper.entityToDetailsResponse(lobby);
  }

  public List<LobbyListResponse> getLobbies(UserEntity loggedInUser) {
    log.info("Getting lobbies for " + loggedInUser.getName());
    return lobbyRepository.findAll().stream()
        .filter(l -> !l.getHidden() && !l.getMembers().contains(loggedInUser))
        .map(lobbyMapper::entityToListResponse)
        .toList();
  }

  public LobbyDetailsResponse createLobby(LobbyCreationRequest request, UserEntity creator) {
    log.info(request.getName() + " creating lobby...");
    LobbyEntity lobby = new LobbyEntity();
    lobbyMapper.map(request, lobby);
    lobby.setOwner(creator.getId());
    lobby.setMembers(List.of(creator));

    while (lobby.getCode() == null || lobbyRepository.existsByCode(lobby.getCode())) {
      lobby.setCode(generateLobbyCode());
      log.info(request.getName() + "Lobby code generated: " + lobby.getCode());
    }

    if (request.getSecured() != null && request.getSecured()) {
      lobby.setPassword(passwordEncoder.encode(request.getPassword()));
    }
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

  public void kickMember(String code, UserEntity user, Long targetUserId) {
    LobbyEntity lobby =
        lobbyRepository
            .findFirstByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));
    List<UserEntity> members = lobby.getMembers();

    if (!Objects.equals(lobby.getOwner(), user.getId())) {
      throw new NoPrivilegeException(lobby.getId(), user.getId());
    }
    if (!members.stream().map(UserEntity::getId).toList().contains(targetUserId)) {
      throw new NotInLobbyException(lobby.getId(), user.getId());
    }

    members.forEach(
        m -> {
          log.info("Broadcasting kick to " + m.getName());
          simpMessagingTemplate.convertAndSendToUser(
              m.getName(),
              "/topic/lobby/update",
              new LobbyUpdateDto(LobbyUpdateTypeEnum.KICK, user.getId()));
        });
    lobby.setMembers(
        members.stream().filter(m -> !Objects.equals(m.getId(), targetUserId)).toList());
    lobbyRepository.save(lobby);
  }

  public void promoteMember(String code, UserEntity user, Long targetUserId) {
    LobbyEntity lobby =
        lobbyRepository
            .findFirstByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(LobbyEntity.class.getName(), code));

    if (!Objects.equals(lobby.getOwner(), user.getId())) {
      throw new NoPrivilegeException(lobby.getId(), user.getId());
    }
    if (!lobby.getMembers().stream().map(UserEntity::getId).toList().contains(targetUserId)) {
      throw new NotInLobbyException(lobby.getId(), user.getId());
    }

    lobby
        .getMembers()
        .forEach(
            m -> {
              log.info("Broadcasting leave to " + m.getName());
              simpMessagingTemplate.convertAndSendToUser(
                  m.getName(),
                  "/topic/lobby/update",
                  new LobbyUpdateDto(LobbyUpdateTypeEnum.OWNER, user.getId()));
            });
    lobby.setOwner(targetUserId);
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
