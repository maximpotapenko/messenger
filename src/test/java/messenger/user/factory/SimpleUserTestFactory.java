package messenger.user.factory;

import lombok.AllArgsConstructor;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class SimpleUserTestFactory implements UserTestFactory {
    private static final AtomicInteger userCounter = new AtomicInteger(0);

    private final PasswordEncoder encoder;

    public User getUser() {
        return User.builder()
                .username("user_" + userCounter.addAndGet(1))
                .password(encoder.encode("password"))
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();
    }

    public RegistrationRequestDto getRegistrationRequestDto() {
        return RegistrationRequestDto.builder()
                .username("user_" + userCounter.addAndGet(1))
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .email("email@hotmail.com")
                .build();
    }
}
