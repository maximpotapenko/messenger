package messaging.api.user.config;

import messaging.api.user.factory.SimpleUserTestFactory;
import messaging.api.user.factory.UserTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {
    @Bean
    public UserTestFactory userTestFactory() {
        return new SimpleUserTestFactory();
    }
}
