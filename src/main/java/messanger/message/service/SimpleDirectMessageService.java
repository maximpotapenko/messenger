package messanger.message.service;

import messanger.message.dto.DirectMessageListResponseDto;
import messanger.message.entity.DirectMessage;
import messanger.message.dto.DirectMessageRequestDto;
import messanger.message.dto.DirectMessageResponseDto;
import messanger.message.mapper.DirectMessageMapper;
import messanger.message.repository.DirectMessageRepository;
import messanger.util.exception.NotYourResourceException;
import messanger.util.exception.ResourceNotFoundException;
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

    private static final String ACCESS_VIOLATION_EXCEPTION_MESSAGE = "This message does not belong to you";

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
