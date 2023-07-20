package messenger.message.factory;

import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.entity.DirectMessage;
import messenger.user.entity.User;

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
    public DirectMessageRequestDto getDirectMessageRequestDto(Long recipient) {
        return DirectMessageRequestDto.builder()
                .recipientId(recipient)
                .message("blank message")
                .build();
    }
}
