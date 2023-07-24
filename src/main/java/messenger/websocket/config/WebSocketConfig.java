package messenger.websocket.config;

import messenger.websocket.handler.CustomHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String APP_DESTINATION_PREFIX = "/app";

    public static final String HANDSHAKE_ENDPOINT = "/connect";

    private static final String SUBSCRIBE_PATH = "/queue";

    private static final String USER_SUBSCRIBE_PATH = "/user" + SUBSCRIBE_PATH;

    public static final String DIRECT_MESSAGE_SUBSCRIBE_PATH = USER_SUBSCRIBE_PATH + "/messages";

    public static final String DIRECT_MESSAGE_SEND_PATH = SUBSCRIBE_PATH + "/messages";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(SUBSCRIBE_PATH);
        registry.setApplicationDestinationPrefixes(APP_DESTINATION_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(HANDSHAKE_ENDPOINT)
                .setHandshakeHandler(new CustomHandshakeHandler());

        registry.addEndpoint(HANDSHAKE_ENDPOINT)
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }
}
