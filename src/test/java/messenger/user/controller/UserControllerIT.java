package messenger.user.controller;

import messenger.user.config.UserTestConfig;
import messenger.user.dto.ProfileListResponseDto;
import messenger.user.dto.ProfileResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.dto.UpdateUserRequestDto;
import messenger.user.entity.Role;
import messenger.user.entity.User;
import messenger.user.repository.UserRepository;
import messenger.user.factory.UserTestFactory;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserTestConfig.class)
@DisplayName("UserController integration test")
class UserControllerIT {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    UserRepository repository;
    @Autowired
    UserTestFactory userTestFactory;

    @Autowired
    PasswordEncoder encoder;

    String auth = "Basic " + Base64.toBase64String("admin:password".getBytes(StandardCharsets.UTF_8));

    @BeforeEach
    void setup() {
        User admin = User.builder()
                .username("admin")
                .password(encoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        admin.setRoles(roles);

        repository.saveAndFlush(admin);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void getUser() {
        //given
        User user = userTestFactory.getUser();

        repository.saveAndFlush(user);

        Long id = user.getId();

        //when //then
        ProfileResponseDto response = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserController.FIND_BY_ID).build(id))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProfileResponseDto.class)
                .getResponseBody().blockFirst();

        assertEquals(user.getId(), response.getId());
        assertEquals(user.getUsername(), response.getUsername());
    }

    @Test
    void findUsersByUsername() {
        //given
            User user1 = userTestFactory.getUser();
                user1.setUsername("crazy_nickname");
            User user2 = userTestFactory.getUser();
                user2.setUsername("crazy_nickname_1");
            User user3 = userTestFactory.getUser();
                user3.setUsername("crazy_nickname_11");
            User user4 = userTestFactory.getUser();
                user4.setUsername("crazy_nickname_333");

            List<User> users = List.of(user3, user2, user1, user4);

            List<String> expectedOrder = Stream.of(user1,user2,user3,user4)
                    .map(User::getUsername)
                    .toList();
        //when //then
            repository.saveAll(users);

            ProfileListResponseDto response = webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(UserController.FIND_USERS_BY_USERNAME)
                            .queryParam("username", "crazy_nickname")
                            .queryParam("offset", 0)
                            .queryParam("limit", 4)
                            .build()
                    )
                    .header(HttpHeaders.AUTHORIZATION, auth)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(ProfileListResponseDto.class)
                    .getResponseBody().blockFirst();

            List<ProfileResponseDto> list = response.getList();

            List<String> actualOrder = list.stream()
                    .map(ProfileResponseDto::getUsername)
                    .toList();

            assertEquals(users.size(), list.size());
            assertIterableEquals(expectedOrder, actualOrder);
    }

    @Test
    void createUser() {
        //given
        RegistrationRequestDto dto = userTestFactory.getRegistrationRequestDto();
        //when //then
            webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path(UserController.CREATE_USER).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(dto))
                    .exchange()
                    .expectStatus().isCreated();

            User user = repository.findByUsername(dto.getUsername()).orElseThrow();

            assertEquals(dto.getFirstName(), user.getFirstName());
            assertEquals(dto.getLastName(), user.getLastName());
            assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void addRole() {
        //given
        User user = userTestFactory.getUser();
        repository.saveAndFlush(user);
        Long id = user.getId();
        //when //then
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(UserController.ADD_ROLE)
                        .queryParam("name", "ROLE_ADMIN")
                        .build(id))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();

        Role role = user.getRoles().stream()
                .filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElse(null);

        assertNotNull(role);
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void deleteRole() {
        //given
        User user = repository.findByUsername("admin").orElseThrow();
        Long id = user.getId();
        //when //then
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(UserController.REMOVE_ROLE)
                        .queryParam("name", "ROLE_ADMIN")
                        .build(id))
                .header(HttpHeaders.AUTHORIZATION,auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();

        Role role = user.getRoles().stream()
                .filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElse(null);

        assertNull(role);
    }

    @Test
    void updateUser() {
        //given
        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .firstName("Jane")
                .lastName("Last")
                .email("newmail@gmail.com")
                .build();
        Long id = repository.findByUsername("admin").orElseThrow().getId();
        //when //then
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(UserController.UPDATE_ACCOUNT)
                        .build())
                .body(BodyInserters.fromValue(request))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        User user = repository.findById(id).orElseThrow();
        assertEquals(request.getFirstName(), user.getFirstName());
        assertEquals(request.getLastName(), user.getLastName());
        assertEquals(request.getEmail(), user.getEmail());
    }

    @Test
    void banUser() {
        //given
        User user = userTestFactory.getUser();

        repository.saveAndFlush(user);

        Long id = user.getId();
        //when //then
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(UserController.BAN_USER).build(id))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();
        assertTrue(user.isBanned());
    }

    @Test
    void unbanUser() {
        //given
        User user = userTestFactory.getUser();

        user.setBanned(true);

        repository.saveAndFlush(user);

        Long id = user.getId();
        //when //then
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(UserController.UNBAN_USER).build(id))
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();
        assertFalse(user.isBanned());
    }

    @Test
    void restoreAccount() {
        User user = userTestFactory.getUser();

        String username = user.getUsername();
        String password = "password";

        user.setDeleted(true);

        repository.saveAndFlush(user);

        String auth = "Basic " + Base64.toBase64String((username + ":" + password).getBytes());

        Long id = user.getId();
        //when //then
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path(UserController.RESTORE_ACCOUNT).build())
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();
        assertFalse(user.isDeleted());
    }

    @Test
    void deleteAccount() {
        //given
        User user = userTestFactory.getUser();

        String username = user.getUsername();
        String password = "password";

        repository.saveAndFlush(user);

        String auth = "Basic " + Base64.toBase64String((username + ":" + password).getBytes());

        Long id = user.getId();
        //when //then
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path(UserController.DELETE_ACCOUNT).build())
                .header(HttpHeaders.AUTHORIZATION, auth)
                .exchange()
                .expectStatus().isOk();

        user = repository.findById(id).orElseThrow();
        assertTrue(user.isDeleted());
    }
}