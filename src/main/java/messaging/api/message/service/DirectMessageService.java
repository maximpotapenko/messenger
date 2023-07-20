package messaging.api.message.service;

import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;

import java.util.List;

public interface DirectMessageService {

    Long createMessage(DirectMessageRequestDto dto);

    DirectMessageResponseDto findDirectMessage(Long id);

    List<DirectMessageResponseDto> fetchConversation(Long userOne, Long userTwo, int offset, int limit);

    void updateMessage(Long id, String value);

    void deleteMessage(Long id);

}
