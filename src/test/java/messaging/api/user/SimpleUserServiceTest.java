package messaging.api.user;

import messaging.api.user.entity.User;
import messaging.api.user.dto.RegistrationRequestDto;
import messaging.api.user.mapper.UserMapper;
import messaging.api.user.repository.UserRepository;
import messaging.api.user.service.SimpleUserService;
import messaging.api.user.service.interfaces.RoleService;
import messaging.api.user.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        userService = new SimpleUserService(userRepository, passwordEncoder, roleService, userMapper);
    }

    @Nested
    class CreateUser {
        @Test
        void shouldSaveTheUserWithoutExceptions() {
            //given
            RegistrationRequestDto dto = RegistrationRequestDto.builder().build();

            String username = "user_1";

            //when
            when(userMapper.toEntity(dto)).thenReturn(User
                    .builder()
                    .username(username)
                    .build());
            when(userRepository.existsUserEntityByUsername(username)).thenReturn(false);

            //then
            assertDoesNotThrow(() -> userService.createUser(dto));
        }

        @Test
        void shouldThrowAnExceptionWhenSavingExistingUser() {
            //given
            RegistrationRequestDto dto = RegistrationRequestDto.builder().build();

            String username = "user_1";

            //when
            when(userMapper.toEntity(dto)).thenReturn(User
                    .builder()
                    .username(username)
                    .build());
            when(userRepository.existsUserEntityByUsername(username)).thenReturn(true);

            //then
            assertThrows(RuntimeException.class, () -> userService.createUser(dto));
        }
    }

    @Nested
    class ChangePassword {

        @Test
        void shouldThrowAnExceptionIfPasswordDoesntMatch() {
            //given
                Long id = 1L;

                User user = User.builder().password("123").build();

                String oldPassword = "1234";

                String newPassword = "5321";

            //when
                when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
                when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

            //then
                assertThrows(RuntimeException.class, () -> userService.changePassword(id, oldPassword, newPassword));
        }

        @Test
        void shouldNotThrowAnExceptionWhenPasswordMatches() {
            //given
            Long id = 1L;

            User user = User.builder().password("123").build();

            String oldPassword = "123";

            String newPassword = "5321";

            //when
            when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
            when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);

            //then
            assertDoesNotThrow(() -> userService.changePassword(id, oldPassword, newPassword));
        }
    }
}
