package messenger.websocket.handler;

import messenger.authentication.ExtendedUserDetails;
import messenger.websocket.principal.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler handler,
                                      Map<String, Object> attributes) {

        ExtendedUserDetails ud = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new StompPrincipal(ud.getId().toString());
    }
}
