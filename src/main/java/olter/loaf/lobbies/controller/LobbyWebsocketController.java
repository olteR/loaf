package olter.loaf.lobbies.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import olter.loaf.lobbies.dto.LobbyUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LobbyWebsocketController {

  @MessageMapping("/lobby/update")
  @SendTo("/topic/lobby/update")
  ResponseEntity<LobbyUpdateDto> sendLobbyUpdate(LobbyUpdateDto lobbyUpdate) {
    return ResponseEntity.ok().body(lobbyUpdate);
  }
}
