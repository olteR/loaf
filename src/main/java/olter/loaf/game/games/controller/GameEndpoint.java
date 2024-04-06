package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameStateResponse;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameEndpoint {
    private final GameService gameService;

    @GetMapping("/game/{code}/details")
    public ResponseEntity<GameDetailsResponse> getGameDetails(
        @PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(gameService.getGame(code, user));
    }

    @GetMapping("/game/{id}/state")
    public ResponseEntity<GameStateResponse> getGameState(
        @PathVariable Long id, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(gameService.getGameState(id, user));
    }
}
