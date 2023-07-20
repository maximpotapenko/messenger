package messenger.message.config;

import messenger.message.factory.DirectMessageTestFactory;
import messenger.message.factory.SimpleDirectMessageTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MessageTestConfig {
    @Bean
    public DirectMessageTestFactory directMessageTestFactory() {
        return new SimpleDirectMessageTestFactory();
    }
}
