package messaging.api.message.service;

import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;

import java.util.List;

public interface DirectMessageService {

    Long createMessage(Long requesterId, DirectMessageRequestDto dto);

    DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId);

    List<DirectMessageResponseDto> fetchConversation(Long userOne, Long userTwo, int offset, int limit);

    void updateMessage(Long requesterId, Long messageId, String value);

    void deleteMessage(Long requesterId, Long messageId);

}
