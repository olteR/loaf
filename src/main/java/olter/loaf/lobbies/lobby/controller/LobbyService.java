package olter.loaf.lobbies.lobby.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.lobbies.lobby.LobbyMapper;
import olter.loaf.lobbies.lobby.dto.LobbyListResponse;
import olter.loaf.lobbies.lobby.model.LobbyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {
	private final LobbyRepository lobbyRepository;
	private final LobbyMapper lobbyMapper;

	public List<LobbyListResponse> getLobbies() {
		return lobbyRepository.findAll().stream().map(lobbyMapper::lobbyEntityToListResponse).toList();
	}
}
