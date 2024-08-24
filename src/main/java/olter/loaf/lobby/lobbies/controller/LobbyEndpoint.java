package olter.loaf.lobby.lobbies.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.lobby.lobbies.dto.LobbyCreationRequest;
import olter.loaf.lobby.lobbies.dto.LobbyDetailsResponse;
import olter.loaf.lobby.lobbies.dto.LobbyListResponse;
import olter.loaf.lobby.lobbies.dto.LobbyMemberInteractionDto;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LobbyEndpoint {
    private final LobbyService lobbyService;

    @GetMapping("/lobbies")
    public ResponseEntity<List<LobbyListResponse>> getLobbies(
        @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(lobbyService.getLobbies(user));
    }

    @GetMapping("/my-games")
    public ResponseEntity<List<LobbyListResponse>> getMyGames(
        @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(lobbyService.getMyGames(user));
    }

    @GetMapping("/lobby/{code}")
    public ResponseEntity<LobbyDetailsResponse> getLobbyDetails(
        @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(lobbyService.getLobby(code, user));
    }

    @PostMapping("/lobby/{code}/start")
    public ResponseEntity<Void> startGame(
        @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        lobbyService.startGame(code, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby")
    public ResponseEntity<LobbyDetailsResponse> createLobby(
        @RequestBody LobbyCreationRequest request,
        @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(lobbyService.createLobby(request, user));
    }

    @PatchMapping("/lobby/{code}/join")
    public ResponseEntity<LobbyDetailsResponse> joinLobby(
        @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(lobbyService.joinLobby(code, user));
    }

    @PostMapping("/lobby/{code}/leave")
    public ResponseEntity<Void> leaveLobby(
        @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        lobbyService.leaveLobby(code, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/kick")
    public ResponseEntity<Void> kickMember(
        @SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbyMemberInteractionDto req) {
        lobbyService.kickMember(user, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/promote")
    public ResponseEntity<Void> promoteMember(
        @SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbyMemberInteractionDto req) {
        lobbyService.promoteMember(user, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lobby/{code}/delete")
    public ResponseEntity<Void> deleteLobby(
        @PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        lobbyService.deleteLobby(code, user);
        return ResponseEntity.ok().build();
    }
}
