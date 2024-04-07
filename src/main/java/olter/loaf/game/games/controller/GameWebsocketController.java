package olter.loaf.game.games.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import olter.loaf.game.games.dto.GameUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class GameWebsocketController {

    @MessageMapping("/game/update")
    @SendTo("/topic/game/update")
    ResponseEntity<GameUpdateDto> sendGameUpdate(GameUpdateDto gameUpdate) {
        return ResponseEntity.ok().body(gameUpdate);
    }
}
