package olter.loaf.lobbies.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWebsocketController {
  @MessageMapping("/ws/lobby/{code}")
  @SendTo("topic/update")
  ResponseEntity<String> sendLobbyUpdate(@DestinationVariable String code) {
    return ResponseEntity.ok("hello");
  }
}
