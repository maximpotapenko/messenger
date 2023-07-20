package messenger.message.service;

import messenger.message.dto.DirectMessageListResponseDto;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;

public interface DirectMessageService {

    Long createMessage(Long requesterId, DirectMessageRequestDto dto);

    DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId);

    DirectMessageListResponseDto fetchConversation(Long userOne, Long userTwo, int offset, int limit);

    void updateMessage(Long requesterId, Long messageId, String value);

    void deleteMessage(Long requesterId, Long messageId);

}
