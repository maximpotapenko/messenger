package messaging.api.message.service;

import messaging.api.message.dto.DirectMessageListResponseDto;
import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;

public interface DirectMessageService {

    Long createMessage(Long requesterId, DirectMessageRequestDto dto);

    DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId);

    DirectMessageListResponseDto fetchConversation(Long userOne, Long userTwo, int offset, int limit);

    void updateMessage(Long requesterId, Long messageId, String value);

    void deleteMessage(Long requesterId, Long messageId);

}
