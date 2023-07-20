package messanger.user.config;

import messanger.user.factory.SimpleUserTestFactory;
import messanger.user.factory.UserTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {
    @Bean
    public UserTestFactory userTestFactory() {
        return new SimpleUserTestFactory();
    }
}
