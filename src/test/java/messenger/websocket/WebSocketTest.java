package messenger.websocket;

import messenger.message.controller.DirectMessageController;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;
import messenger.message.repository.DirectMessageRepository;
import messenger.user.entity.User;
import messenger.user.repository.UserRepository;
import messenger.websocket.config.WebSocketConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("WebSocket integration tests")
class WebSocketTest {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DirectMessageRepository directMessageRepository;

    @Autowired
    PasswordEncoder encoder;

    WebSocketStompClient webSocketStompClient;

    CountDownLatch lock = new CountDownLatch(1);

    String auth = "Basic " + Base64.toBase64String("admin:password".getBytes());

    Long id;

    @BeforeEach
    void setup() {

        webSocketStompClient = new WebSocketStompClient(new StandardWebSocketClient());

        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        webSocketStompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        User admin = User.builder()
                .username("admin")
                .password(encoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();

        userRepository.saveAndFlush(admin);

        id = admin.getId();
    }

    @AfterEach
    void tearDown() {
        directMessageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldNotifyUserWhenCreatedNewDirectMessage() throws Exception {
        //given
        StompHeaders headers = new StompHeaders();

        headers.add("Authorization", auth);

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

        webSocketHttpHeaders.add("Authorization", auth);

        String handshakeEndpoint = "ws://localhost:%d".formatted(port) + WebSocketConfig.HANDSHAKE_ENDPOINT;

        StompSession session = webSocketStompClient.connectAsync(handshakeEndpoint,
                webSocketHttpHeaders,
                headers,
                new StompSessionHandlerAdapter() {
                }).get();

        BlockingQueue<DirectMessageResponseDto> responses = new ArrayBlockingQueue<>(1);

        String message = "message to admin";

        //when
        session.subscribe(WebSocketConfig.DIRECT_MESSAGE_SUBSCRIBE_PATH, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return DirectMessageResponseDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                responses.add((DirectMessageResponseDto) payload);
            }
        });

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.CREATE_MESSAGE).build())
                .header(HttpHeaders.AUTHORIZATION, auth)
                .body(BodyInserters.fromValue(
                        DirectMessageRequestDto.builder()
                                .recipientId(id)
                                .message(message)
                                .build()
                )).exchange();

        lock.await(1, TimeUnit.SECONDS);

        //then
        responseSpec.expectStatus().isCreated();

        assertEquals(1, responses.size());

        DirectMessageResponseDto response = responses.poll();

        assertEquals(id, response.getAuthorId());
        assertEquals(id, response.getRecipientId());
        assertEquals(message, response.getMessage());
    }
}
