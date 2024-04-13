package olter.loaf.common.ws;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import olter.loaf.common.security.JwtHandler;
import olter.loaf.common.security.LoafPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class LoafWebsocketHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtHandler jwtHandler;

    @Override
    protected Principal determineUser(
        ServerHttpRequest request,
        WebSocketHandler webSocketHandler,
        Map<String, Object> attributes) {
        if (!request.getURI().getQuery().isBlank()) {
            String user = jwtHandler.getUserIdFromToken(request.getURI().getQuery());
            log.info("Websocket authenticated for user: " + user);
            return new LoafPrincipal(user);
        }
        return super.determineUser(request, webSocketHandler, attributes);
    }
}
