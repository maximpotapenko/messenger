package messenger.user.factory;

import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleUserTestFactory implements UserTestFactory {
    private static final AtomicInteger userCounter = new AtomicInteger(0);

    public User getUser() {
        return User.builder()
                .username("user_" + userCounter.addAndGet(1))
                .password(new BCryptPasswordEncoder().encode("password"))
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
