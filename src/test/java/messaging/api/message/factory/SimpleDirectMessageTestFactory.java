package messaging.api.message.factory;

import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.entity.DirectMessage;
import messaging.api.user.entity.User;

import java.time.Instant;

public class SimpleDirectMessageTestFactory implements DirectMessageTestFactory {
    @Override
    public DirectMessage getDirectMessage(Long author, Long recipient) {
        return DirectMessage.builder()
                .author(User.builder().id(author).build())
                .recipient(User.builder().id(recipient).build())
                .createdAt(Instant.now())
                .message("blank message")
                .build();
    }

    @Override
    public DirectMessageRequestDto getDirectMessageRequestDto(Long author, Long recipient) {
        return DirectMessageRequestDto.builder()
                .authorId(author)
                .recipientId(recipient)
                .message("blank message")
                .build();
    }
}
