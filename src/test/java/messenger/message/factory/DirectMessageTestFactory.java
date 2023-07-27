package messenger.message.factory;

import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.entity.DirectMessage;

public interface DirectMessageTestFactory {

    DirectMessage getDirectMessage(Long author, Long recipient);

    DirectMessageRequestDto getDirectMessageRequestDto(Long recipient);
}
