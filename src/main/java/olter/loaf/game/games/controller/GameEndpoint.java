package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.cards.dto.AbilityRequest;
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
    public ResponseEntity<List<DistrictResponse>> drawCards(@PathVariable String code,
        @RequestBody List<Integer> indexes, @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        return ResponseEntity.ok().body(gameService.drawCards(code, indexes, user));
    }

    @GetMapping("game/{code}/build")
    public ResponseEntity<Void> buildDistrict(@PathVariable String code, @RequestParam Integer index,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        gameService.buildDistrict(code, index, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("game/ability")
    public ResponseEntity<Void> useAbility(@RequestBody AbilityRequest request,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) throws IllegalAccessException {
        gameService.useAbility(request, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("game/{code}/end-turn")
    public ResponseEntity<List<Integer>> endTurn(@PathVariable String code,
        @SecurityAnnotations.GetLoggedInUser UserEntity user
    ) {
        gameService.endTurn(code, user);
        return ResponseEntity.ok().build();
    }
}
