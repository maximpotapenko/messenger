package messaging.api.message.config;

import messaging.api.message.factory.DirectMessageTestFactory;
import messaging.api.message.factory.SimpleDirectMessageTestFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MessageTestConfig {
    @Bean
    public DirectMessageTestFactory directMessageTestFactory() {
        return new SimpleDirectMessageTestFactory();
    }
}
