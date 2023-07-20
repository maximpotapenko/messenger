package messaging.api.message.service;

import messaging.api.message.entity.DirectMessage;
import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;
import messaging.api.message.mapper.DirectMessageMapper;
import messaging.api.message.repository.DirectMessageRepository;
import messaging.api.util.exception.NotYourResourceException;
import messaging.api.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SimpleDirectMessageService implements DirectMessageService{

    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "Message doesn't exist";

    @Override
    public Long createMessage(Long requesterId, DirectMessageRequestDto dto) {
        DirectMessage message = directMessageMapper.toEntity(dto);

        message.setId(requesterId);

        directMessageRepository.saveAndFlush(message);

        return message.getId();
    }

    @Override
    public DirectMessageResponseDto findDirectMessage(Long requesterId, Long messageId) {
        DirectMessageResponseDto response = directMessageRepository.findById(messageId)
                .map(directMessageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(!response.getAuthorId().equals(requesterId) && !response.getRecipientId().equals(requesterId))
            throw new NotYourResourceException("This message does not belong to you");

        return response;
    }

    @Override
    public List<DirectMessageResponseDto> fetchConversation(Long userOne, Long userTwo, int offset, int limit) {
        return directMessageRepository.findAllByAuthorIdOrRecipientId(userOne, userTwo, offset, limit)
                .stream()
                .map(directMessageMapper::toDto)
                .toList();
    }

    @Override
    public void updateMessage(Long requesterId, Long messageId, String value) {
        DirectMessage directMessage = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(directMessage.getAuthor().getId() != requesterId)
            throw new NotYourResourceException("This message does not belong to you");

        directMessage.setMessage(value);

        directMessageRepository.saveAndFlush(directMessage);
    }

    @Override
    public void deleteMessage(Long requesterId, Long messageId) {
        DirectMessage directMessage = directMessageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        if(directMessage.getAuthor().getId() != requesterId)
            throw new NotYourResourceException("This message does not belong to you");

        directMessageRepository.deleteById(messageId);
    }
}
