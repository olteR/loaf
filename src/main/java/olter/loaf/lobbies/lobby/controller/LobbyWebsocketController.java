package olter.loaf.lobbies.lobby.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyWebsocketController {
  @MessageMapping("/ws")
  @SendTo("topic/greetings")
  ResponseEntity<String> greet() {
    return ResponseEntity.ok("hello");
  }
}
