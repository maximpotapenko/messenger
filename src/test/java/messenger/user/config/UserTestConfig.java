package messenger.user.config;

import messenger.user.factory.SimpleUserTestFactory;
import messenger.user.factory.UserTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class UserTestConfig {
    @Bean
    public UserTestFactory userTestFactory(PasswordEncoder encoder) {
        return new SimpleUserTestFactory(encoder);
    }
}
