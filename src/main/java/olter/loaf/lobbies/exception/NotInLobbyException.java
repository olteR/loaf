package olter.loaf.lobbies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotInLobbyException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public <T> NotInLobbyException(Long lobbyId, Long userId) {
    super("user " + userId + " unauthorized to access " + lobbyId);
  }
}
