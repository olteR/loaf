package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.games.dto.GameDetailsResponse;
import olter.loaf.game.games.model.ResourceTypeEnum;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("game/{code}/select")
    public ResponseEntity<List<Integer>> selectCharacter(@PathVariable String code, @RequestParam Integer character,
                                                         @SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(gameService.selectCharacter(code, character, user));
    }

    @GetMapping("game/{code}/resource")
    public ResponseEntity<List<DistrictResponse>> gatherResources(@PathVariable String code,
                                                                  @RequestParam ResourceTypeEnum type,
                                                                  @SecurityAnnotations.GetLoggedInUser
                                                                  UserEntity user) {
        return ResponseEntity.ok().body(gameService.gatherResources(code, type, user));
    }
}
