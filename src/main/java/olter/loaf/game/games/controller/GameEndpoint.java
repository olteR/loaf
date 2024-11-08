package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.cards.dto.DistrictResponse;
import olter.loaf.game.games.dto.DistrictBuildRequest;
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
    public ResponseEntity<GameDetailsResponse> getGameDetails(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(gameService.getGame(code, user));
    }

    @GetMapping("game/{code}/select")
    public ResponseEntity<List<Integer>> selectCharacter(@PathVariable String code, @RequestParam Integer character,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(gameService.selectCharacter(code, character, user));
    }

    @GetMapping("game/{code}/resource")
    public ResponseEntity<List<DistrictResponse>> gatherResources(@PathVariable String code,
        @RequestParam ResourceTypeEnum type, @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(gameService.gatherResources(code, type, user));
    }

    @PostMapping("game/{code}/cards")
    public ResponseEntity<List<DistrictResponse>> drawCards(@PathVariable String code, @RequestBody List<Integer> indexes,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(gameService.drawCards(code, indexes, user));
    }

    @PostMapping("game/{code}/build")
    public ResponseEntity<Void> buildDistrict(@PathVariable String code, @RequestBody DistrictBuildRequest request,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        gameService.buildDistrict(code, request, user);
        return ResponseEntity.ok().build();
    }
}
