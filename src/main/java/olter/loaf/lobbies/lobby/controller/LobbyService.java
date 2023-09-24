package olter.loaf.lobbies.lobby.controller;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.common.exception.ResourceNotFoundException;
import olter.loaf.lobbies.lobby.LobbyMapper;
import olter.loaf.lobbies.lobby.dto.LobbyCreationRequest;
import olter.loaf.lobbies.lobby.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.lobbies.lobby.exception.AlreadyJoinedException;
import olter.loaf.lobbies.lobby.exception.LobbyFullException;
import olter.loaf.lobbies.lobby.exception.NotInLobbyException;
import olter.loaf.lobbies.lobby.model.LobbyEntity;
import olter.loaf.lobbies.lobby.model.LobbyRepository;
import olter.loaf.users.model.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {
  private final LobbyRepository lobbyRepository;
  private final LobbyMapper lobbyMapper;

  private final PasswordEncoder passwordEncoder;

  public List<LobbyListResponse> getMyGames(UserEntity loggedInUser) {
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
    if (!lobby.getMembers().contains(loggedInUser)) {
      throw new NotInLobbyException(lobby.getId(), loggedInUser.getId());
    }
    return lobbyMapper.entityToDetailsResponse(lobby);
  }

  public List<LobbyListResponse> getLobbies(UserEntity loggedInUser) {
    return lobbyRepository.findAll().stream()
        .filter(l -> !l.getHidden() && !l.getMembers().contains(loggedInUser))
        .map(lobbyMapper::entityToListResponse)
        .toList();
  }

  public LobbyDetailsResponse createLobby(LobbyCreationRequest request, UserEntity creator) {
    log.info(request.getName());
    LobbyEntity lobby = new LobbyEntity();
    lobbyMapper.map(request, lobby);
    lobby.setOwner(creator.getId());
    lobby.setMembers(List.of(creator));

    while (lobby.getCode() == null || lobbyRepository.existsByCode(lobby.getCode())) {
      lobby.setCode(generateLobbyCode());
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

    if (members.contains(user)) {
      throw new AlreadyJoinedException(lobby.getId(), user.getId());
    }
    if (lobby.getMaxMembers() == members.size()) {
      throw new LobbyFullException(lobby.getId());
    }

    members.add(user);
    lobby.setMembers(members);
    lobbyRepository.save(lobby);
    return lobbyMapper.entityToDetailsResponse(lobby);
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
