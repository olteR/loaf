package olter.loaf.lobbies.lobby.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import olter.loaf.lobbies.lobby.dto.LobbyCreationRequest;
import olter.loaf.lobbies.lobby.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.security.SecurityAnnotations;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LobbyEndpoint {
  private final LobbyService lobbyService;

  @GetMapping("/lobbies")
  public ResponseEntity<List<LobbyListResponse>> getLobbies() {
    return ResponseEntity.ok().body(lobbyService.getLobbies());
  }

  @PostMapping("/lobby")
  public ResponseEntity<LobbyDetailsResponse> createLobby(
      @RequestBody LobbyCreationRequest request,
      @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.createLobby(request, user));
  }
}
