package olter.loaf.game.cards.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.game.cards.dto.CardsResponse;
import olter.loaf.game.cards.dto.CharacterResponse;
import olter.loaf.game.cards.dto.DistrictResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardEndpoint {

    private final CardService cardService;

    @GetMapping("/game/characters")
    public ResponseEntity<List<CharacterResponse>> getAllCharacters() {
        return ResponseEntity.ok().body(cardService.getAllCharacters());
    }

    @GetMapping("/game/districts")
    public ResponseEntity<List<DistrictResponse>> getAllDistricts() {
        return ResponseEntity.ok().body(cardService.getAllDistricts());
    }

    @GetMapping("/game/cards")
    public ResponseEntity<CardsResponse> getAllCards() {
        return ResponseEntity.ok().body(cardService.getAllCards());
    }
}
