package olter.loaf.lobbies.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import olter.loaf.lobbies.dto.LobbyCreationRequest;
import olter.loaf.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobbies.dto.LobbyListResponse;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LobbyEndpoint {
  private final LobbyService lobbyService;

  @GetMapping("/my-games")
  public ResponseEntity<List<LobbyListResponse>> getMyGames(
      @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.getMyGames(user));
  }

  @GetMapping("/lobby/{code}")
  public ResponseEntity<LobbyDetailsResponse> getLobbies(
      @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.getLobby(code, user));
  }

  @GetMapping("/lobbies")
  public ResponseEntity<List<LobbyListResponse>> getLobbies(
      @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.getLobbies(user));
  }

  @PostMapping("/lobby")
  public ResponseEntity<LobbyDetailsResponse> createLobby(
      @RequestBody LobbyCreationRequest request,
      @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.createLobby(request, user));
  }

  @PatchMapping("/join/{code}")
  public ResponseEntity<LobbyDetailsResponse> joinLobby(
      @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    return ResponseEntity.ok().body(lobbyService.joinLobby(code, user));
  }

  @PostMapping("leave/{code}")
  public ResponseEntity<Void> leaveLobby(@PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
    lobbyService.leaveLobby(code, user);
    return ResponseEntity.ok().build();
  }
}
