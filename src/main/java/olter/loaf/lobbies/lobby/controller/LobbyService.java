package olter.loaf.lobbies.lobby.controller;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.lobbies.lobby.LobbyMapper;
import olter.loaf.lobbies.lobby.dto.LobbyCreationRequest;
import olter.loaf.lobbies.lobby.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
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

  public List<LobbyListResponse> getLobbies() {
    return lobbyRepository.findAll().stream()
        .filter(not(LobbyEntity::getHidden))
        .map(lobbyMapper::entityToListResponse)
        .toList();
  }

  public LobbyDetailsResponse createLobby(LobbyCreationRequest request, UserEntity creator) {
    log.info(request.getName());
    LobbyEntity lobby = new LobbyEntity();
    lobbyMapper.map(request, lobby);
    lobby.setCode(generateLobbyCode());
    lobby.setOwner(creator.getId());
    lobby.setMembers(List.of(creator));
    if (request.isSecured()) {
      lobby.setPassword(passwordEncoder.encode(request.getPassword()));
    }
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
