package messanger.message.service;

import messanger.message.dto.DirectMessageListResponseDto;
import messanger.message.dto.DirectMessageRequestDto;
import messanger.message.dto.DirectMessageResponseDto;

public interface DirectMessageService {

    Long createMessage(Long requesterId, DirectMessageRequestDto dto);

    DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId);

    DirectMessageListResponseDto fetchConversation(Long userOne, Long userTwo, int offset, int limit);

    void updateMessage(Long requesterId, Long messageId, String value);

    void deleteMessage(Long requesterId, Long messageId);

}
