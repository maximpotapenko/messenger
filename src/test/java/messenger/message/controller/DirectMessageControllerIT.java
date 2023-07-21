package messenger.message.controller;

import messenger.message.config.MessageTestConfig;
import messenger.message.dto.DirectMessageListResponseDto;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;
import messenger.message.dto.UpdateMessageRequestDto;
import messenger.message.entity.DirectMessage;
import messenger.message.factory.DirectMessageTestFactory;
import messenger.message.repository.DirectMessageRepository;
import messenger.user.entity.User;
import messenger.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MessageTestConfig.class)
@DisplayName("DirectMessageController integration test")
class DirectMessageControllerIT {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DirectMessageRepository directMessageRepository;
    @Autowired
    DirectMessageTestFactory messageFactory;

    User author;
    User recipient;
    String auth;

    @BeforeEach
    void setupAll() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        author = User.builder()
                .username("author")
                .password(encoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();

        recipient = User.builder()
                .username("recipient")
                .password(encoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();

        auth = "Basic " + Base64.toBase64String((author.getUsername() + ":password").getBytes());

        userRepository.saveAllAndFlush(List.of(author, recipient));
    }

    @AfterEach
    void tearDown() {
        directMessageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findMessage() {
        //given
        DirectMessage message = messageFactory.getDirectMessage(author.getId(), recipient.getId());

        directMessageRepository.saveAndFlush(message);

        Long messageId = message.getId();
        //when //then
        DirectMessageResponseDto response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.FIND_MESSAGE).build(messageId))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk()
                .returnResult(DirectMessageResponseDto.class)
                .getResponseBody().blockFirst();

        assertEquals(message.getId(), response.getId());
        assertEquals(message.getAuthor().getId(), response.getAuthorId());
        assertEquals(message.getRecipient().getId(), response.getRecipientId());
        assertEquals(message.getMessage(), response.getMessage());
    }

    @Test
    void fetchConversation() {
        //given
        DirectMessage msg1 = messageFactory.getDirectMessage(author.getId(), recipient.getId());
        DirectMessage msg2 = messageFactory.getDirectMessage(author.getId(), recipient.getId());
        DirectMessage msg3 = messageFactory.getDirectMessage(author.getId(), recipient.getId());
        DirectMessage msg4 = messageFactory.getDirectMessage(author.getId(), recipient.getId());
        List<DirectMessage> list = List.of(msg1, msg2, msg3, msg4);

        directMessageRepository.saveAllAndFlush(list);
        //when //then
        DirectMessageListResponseDto response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.FETCH_CONVERSATION)
                        .queryParam("offset", 0)
                        .queryParam("limit", 4)
                        .build(recipient.getId()))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk()
                .returnResult(DirectMessageListResponseDto.class)
                .getResponseBody().blockFirst();

        List<DirectMessageResponseDto> result = response.getList();

        assertEquals(list.size(), result.size());
    }

    @Test
    void createMessage() {
        //given
        DirectMessageRequestDto request = messageFactory.getDirectMessageRequestDto(recipient.getId());
        //when //then
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.CREATE_MESSAGE).build())
                .body(BodyInserters.fromValue(request))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void updateMessage() {
        //given
        DirectMessage message = messageFactory.getDirectMessage(author.getId(), recipient.getId());

        directMessageRepository.saveAndFlush(message);

        Long messageId = message.getId();

        UpdateMessageRequestDto dto = new UpdateMessageRequestDto("updated message");

        //when //then
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.UPDATE_MESSAGE).build(messageId))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isOk();

        DirectMessage updatedMessage = directMessageRepository.findById(messageId).orElseThrow();

        assertEquals(dto.getMessage(), updatedMessage.getMessage());
    }

    @Test
    void deleteMessage() {
        //given
        DirectMessage message = messageFactory.getDirectMessage(author.getId(), recipient.getId());

        directMessageRepository.saveAndFlush(message);

        Long messageId = message.getId();
        //when //then
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(DirectMessageController.DELETE_MESSAGE).build(messageId))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        Optional<DirectMessage> optional = directMessageRepository.findById(messageId);

        assertThrows(RuntimeException.class, () -> optional.orElseThrow());
    }
}