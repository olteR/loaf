package olter.loaf.game.characters.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.game.characters.dto.CharacterListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CharacterEndpoint {

    private final CharacterService characterService;

    @GetMapping("/game/characters")
    public ResponseEntity<List<CharacterListResponse>> getAllCharacters() {
        return ResponseEntity.ok().body(characterService.getAllCharacters());
    }
}
