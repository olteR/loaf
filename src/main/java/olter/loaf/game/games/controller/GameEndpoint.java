package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.dto.GameStateResponse;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/game/{code}/state")
    public ResponseEntity<GameStateResponse> getGameState(
        @PathVariable String code, @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(gameService.getGameState(code, user));
    }

    @GetMapping("game/{code}/select")
    public ResponseEntity<Void> selectCharacter(@PathVariable String code, @RequestParam Integer character,
                                                @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        gameService.selectCharacter(code, character, user);
        return ResponseEntity.ok().build();
    }
}
