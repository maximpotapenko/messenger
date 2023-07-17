package messaging.api.message.service;

import messaging.api.message.entity.DirectMessage;
import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;
import messaging.api.message.mapper.DirectMessageMapper;
import messaging.api.message.repository.DirectMessageRepository;
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
    public void createMessage(DirectMessageRequestDto dto) {
        DirectMessage message = directMessageMapper.toEntity(dto);

        directMessageRepository.saveAndFlush(message);
    }

    @Override
    public DirectMessageResponseDto findDirectMessage(Long id) {
        return directMessageRepository.findById(id)
                .map(directMessageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));
    }

    @Override
    public List<DirectMessageResponseDto> fetchConversation(Long userOne, Long userTwo, int offset, int limit) {
        return directMessageRepository.findAllByAuthorIdOrRecipientId(userOne, userTwo, offset, limit)
                .stream()
                .map(directMessageMapper::toDto)
                .toList();
    }

    @Override
    public void updateMessage(Long id, String value) {
        DirectMessage directMessage = directMessageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE));

        directMessage.setMessage(value);

        directMessageRepository.saveAndFlush(directMessage);
    }

    @Override
    public void deleteMessage(Long id) {
        if(!directMessageRepository.existsById(id)) throw new ResourceNotFoundException(NOT_FOUND_EXCEPTION_MESSAGE);

        directMessageRepository.deleteById(id);
    }
}
