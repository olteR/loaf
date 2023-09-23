package olter.loaf.lobbies.lobby.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LobbyEndpoint {
	private final LobbyService lobbyService;

	@GetMapping("/lobbies")
	public ResponseEntity<List<LobbyListResponse>> getLobbies() {
		return ResponseEntity.ok().body(lobbyService.getLobbies());
	}
}
