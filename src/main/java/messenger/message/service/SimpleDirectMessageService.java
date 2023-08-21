package messenger.message.service;

import jakarta.transaction.Transactional;
import messenger.message.dto.DirectMessageListResponseDto;
import messenger.message.entity.DirectMessage;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;
import messenger.message.mapper.DirectMessageMapper;
import messenger.message.repository.DirectMessageRepository;
import messenger.common.exception.NotYourResourceException;
import messenger.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messenger.user.entity.User;
import messenger.common.config.WebSocketConfig;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SimpleDirectMessageService implements DirectMessageService{

    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "Message doesn't exist";

    private static final String ACCESS_VIOLATION_EXCEPTION_MESSAGE = "Message doesn't belong to you";

    @Override
    @Transactional
    public Long createMessage(Long requesterId, DirectMessageRequestDto dto) {

        DirectMessage message = directMessageMapper.toEntity(dto);

        message.setAuthor(new User());

        message.getAuthor().setId(requesterId);

        directMessageRepository.saveAndFlush(message);

        DirectMessageResponseDto payload = directMessageMapper.toDto(message);

        simpMessagingTemplate.convertAndSendToUser(dto.getRecipientId().toString(), WebSocketConfig.DIRECT_MESSAGE_SEND_PATH, payload);

        return message.getId();
    }

    @Override
    public DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId) {
        DirectMessageResponseDto response = directMessageRepository.findById(messageId)
                .map(directMessageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(!response.getAuthorId().equals(requesterId) && !response.getRecipientId().equals(requesterId))
            throw new NotYourResourceException(ACCESS_VIOLATION_EXCEPTION_MESSAGE);

        return response;
    }

    @Override
    public DirectMessageListResponseDto fetchConversation(Long userOne, Long userTwo, int offset, int limit) {
        List<DirectMessageResponseDto> list = directMessageRepository
                .findAllByAuthorIdOrRecipientId(userOne, userTwo, offset, limit)
                .stream()
                .map(directMessageMapper::toDto)
                .toList();

        return new DirectMessageListResponseDto(list);
    }

    @Override
    public void updateMessage(Long requesterId, Long messageId, String value) {
        DirectMessage directMessage = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(directMessage.getAuthor().getId() != requesterId)
            throw new NotYourResourceException(ACCESS_VIOLATION_EXCEPTION_MESSAGE);

        directMessage.setMessage(value);

        directMessageRepository.saveAndFlush(directMessage);
    }

    @Override
    public void deleteMessage(Long requesterId, Long messageId) {
        DirectMessage directMessage = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(directMessage.getAuthor().getId() != requesterId)
            throw new NotYourResourceException(ACCESS_VIOLATION_EXCEPTION_MESSAGE);

        directMessageRepository.deleteById(messageId);
    }
}
