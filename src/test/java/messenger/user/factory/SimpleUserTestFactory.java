package messenger.user.factory;

import lombok.AllArgsConstructor;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.Role;
import messenger.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class SimpleUserTestFactory implements UserTestFactory {
    private static final AtomicInteger userCounter = new AtomicInteger(0);

    private final PasswordEncoder encoder;

    private final String rawPassword = "password";

    public User getAdmin() {
            return User.builder()
                .username("admin")
                .password(encoder.encode(rawPassword))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .roles(List.of(Role.builder().name("ROLE_ADMIN").build()))
                .build();
    }

    public User getUser() {
        return User.builder()
                .username("user_" + userCounter.addAndGet(1))
                .password(encoder.encode(rawPassword))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();
    }

    public RegistrationRequestDto getRegistrationRequestDto() {
        return RegistrationRequestDto.builder()
                .username("user_" + userCounter.addAndGet(1))
                .password(rawPassword)
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();
    }

    public String getRawPassword() {
        return rawPassword;
    }
}
