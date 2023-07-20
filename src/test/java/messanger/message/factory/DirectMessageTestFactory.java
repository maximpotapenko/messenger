package messanger.message.factory;

import messanger.message.dto.DirectMessageRequestDto;
import messanger.message.entity.DirectMessage;

public interface DirectMessageTestFactory {
    DirectMessage getDirectMessage(Long author, Long recipient);

    DirectMessageRequestDto getDirectMessageRequestDto(Long recipient);
}
