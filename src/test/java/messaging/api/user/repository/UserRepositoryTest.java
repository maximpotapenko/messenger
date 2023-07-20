package messaging.api.user.repository;

import messaging.api.user.entity.User;
import messaging.api.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
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
            //when
            userRepository.saveAll(users);

            List<User> queryResult = userRepository.findClosestUsersByUsernameLike("user", 0, 4);

            List<String> result = new ArrayList<>();

            for(User i : queryResult) {
                result.add(i.getUsername());
            }
            //then
            assertIterableEquals(expected, result);
        }

        @Test
        void mustReturnEmptyListIfNoUsersFound() {
            //given
            List<User> expected = Collections.emptyList();

            //when
            List<User> result = userRepository.findClosestUsersByUsernameLike("user", 0, 4);

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

            //when
            userRepository.saveAll(users);

            List<User> result = userRepository.findClosestUsersByUsernameLike("user", 1,2);
            //then
            assertEquals(2,result.size());
        }
    }
}
