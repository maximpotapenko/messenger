package messenger.user.service;

import messenger.user.entity.User;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.mapper.UserMapper;
import messenger.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

            given(userMapper.toEntity(dto)).willReturn(User
                    .builder()
                    .username(username)
                    .build());
            given(userRepository.existsUserEntityByUsername(username)).willReturn(false);

            //when //then
            assertDoesNotThrow(() -> userService.createUser(dto));
        }

        @Test
        void shouldThrowAnExceptionWhenSavingExistingUser() {
            //given
            RegistrationRequestDto dto = RegistrationRequestDto.builder().build();

            String username = "user_1";

            given(userMapper.toEntity(dto)).willReturn(User
                    .builder()
                    .username(username)
                    .build());
            given(userRepository.existsUserEntityByUsername(username)).willReturn(true);

            //when //then
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

                given(userRepository.findById(id)).willReturn(Optional.ofNullable(user));
                given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(false);

            //when //then
                assertThrows(RuntimeException.class, () -> userService.changePassword(id, oldPassword, newPassword));
        }

        @Test
        void shouldNotThrowAnExceptionWhenPasswordMatches() {
            //given
            Long id = 1L;

            User user = User.builder().password("123").build();

            String oldPassword = "123";

            String newPassword = "5321";

            given(userRepository.findById(id)).willReturn(Optional.ofNullable(user));
            given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(true);

            //when //then
            assertDoesNotThrow(() -> userService.changePassword(id, oldPassword, newPassword));
        }
    }
}
