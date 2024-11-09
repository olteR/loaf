package olter.loaf.lobbies.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.lobbies.dto.*;
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
    public ResponseEntity<List<LobbyListResponse>> getLobbies(@SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.getLobbies(user));
    }

    @GetMapping("/my-games")
    public ResponseEntity<List<LobbyListResponse>> getMyGames(@SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.getMyGames(user));
    }

    @GetMapping("/lobby/{code}")
    public ResponseEntity<LobbyDetailsResponse> getLobbyDetails(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.getLobby(code, user));
    }

    @PostMapping("/lobby")
    public ResponseEntity<LobbyDetailsResponse> createLobby(@RequestBody LobbyRequest request,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.createLobby(request, user));
    }

    @PostMapping("/lobby/{code}")
    public ResponseEntity<LobbyDetailsResponse> editLobby(@RequestBody LobbyRequest request, @PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.editLobby(request, code, user));
    }

    @PatchMapping("/lobby/{code}/join")
    public ResponseEntity<LobbyDetailsResponse> joinLobby(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(lobbyService.joinLobby(code, user));
    }

    @PostMapping("/lobby/{code}/leave")
    public ResponseEntity<Void> leaveLobby(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        lobbyService.leaveLobby(code, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/promote")
    public ResponseEntity<Void> promoteMember(@SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbyMemberInteractionDto request
    ) {
        lobbyService.promoteMember(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/kick")
    public ResponseEntity<Void> kickMember(@SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbyMemberInteractionDto request
    ) {
        lobbyService.kickMember(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/{code}/reorder")
    public ResponseEntity<Void> reorderLobby(@RequestBody List<LobbyMemberDto> request, @PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        lobbyService.reorderLobby(request, code, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("lobby/characters")
    public ResponseEntity<Void> updateCharacters(@SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbySettingDto request
    ) {
        lobbyService.updateCharacters(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("lobby/districts")
    public ResponseEntity<Void> updateDistricts(@SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbySettingDto req
    ) {
        lobbyService.updateDistricts(user, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("lobby/crown")
    public ResponseEntity<Void> crownMember(@SecurityAnnotations.GetLoggedInUser UserEntity user,
        @RequestBody LobbyMemberInteractionDto req
    ) {
        lobbyService.crownMember(user, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lobby/{code}/start")
    public ResponseEntity<Void> startGame(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        lobbyService.startGame(code, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lobby/{code}/delete")
    public ResponseEntity<Void> deleteLobby(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        lobbyService.deleteLobby(code, user);
        return ResponseEntity.ok().build();
    }
}
