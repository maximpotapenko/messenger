package messenger.message.config;

import messenger.message.factory.DirectMessageTestFactory;
import messenger.message.factory.SimpleDirectMessageTestFactory;
import messenger.user.factory.SimpleUserTestFactory;
import messenger.user.factory.UserTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class MessageTestConfig {
    @Bean
    public DirectMessageTestFactory directMessageTestFactory() {
        return new SimpleDirectMessageTestFactory();
    }

    @Bean
    public UserTestFactory userTestFactory(PasswordEncoder encoder) {
        return new SimpleUserTestFactory(encoder);
    }
}
