package messenger.user.repository;

import messenger.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Nested
    class FindClosestUsersByUsernameLike {
        @Test
        void mustReturnUsersInTheCorrectSequence() {
            //given
            User user1 = User.builder().firstName("John").lastName("Doe").username("user").password("password").email("email@hotmail.com").build();
            User user2 = User.builder().firstName("John").lastName("Doe").username("user1").password("password").email("email@hotmail.com").build();
            User user3 = User.builder().firstName("John").lastName("Doe").username("user11").password("password").email("email@hotmail.com").build();
            User user4 = User.builder().firstName("John").lastName("Doe").username("user333").password("password").email("email@hotmail.com").build();

            List<User> users = List.of(user1,user3,user4,user2);

            List<String> expected = List.of("user", "user1", "user11", "user333");

            String search = "user";

            //when
            userRepository.saveAll(users);

            List<String> result = userRepository.findClosestUsersByUsernameLike(search, 0, 4)
                    .stream()
                    .map(User::getUsername)
                    .toList();

            //then
            assertIterableEquals(expected, result);
        }

        @Test
        void mustReturnEmptyListIfNoUsersFound() {
            //given
            List<User> expected = Collections.emptyList();

            String search = "user";

            //when
            List<User> result = userRepository.findClosestUsersByUsernameLike(search, 0, 4);

            //then
            assertIterableEquals(expected, result);
        }

        @Test
        void mustReturnUsersAccordingToOffsetAndLimit() {
            //given
            User user1 = User.builder().firstName("John").lastName("Doe").username("user").password("password").email("email@hotmail.com").build();
            User user2 = User.builder().firstName("John").lastName("Doe").username("user1").password("password").email("email@hotmail.com").build();
            User user3 = User.builder().firstName("John").lastName("Doe").username("user11").password("password").email("email@hotmail.com").build();
            User user4 = User.builder().firstName("John").lastName("Doe").username("user333").password("password").email("email@hotmail.com").build();

            List<User> users = List.of(user1, user2, user3, user4);

            String search = "user";

            //when
            userRepository.saveAll(users);

            List<User> result = userRepository.findClosestUsersByUsernameLike(search, 1,2);

            //then
            assertEquals(2,result.size());
        }
    }
}
