package messaging.api.message.factory;

import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.entity.DirectMessage;

public interface DirectMessageTestFactory {
    DirectMessage getDirectMessage(Long author, Long recipient);

    DirectMessageRequestDto getDirectMessageRequestDto(Long author, Long recipient);
}
