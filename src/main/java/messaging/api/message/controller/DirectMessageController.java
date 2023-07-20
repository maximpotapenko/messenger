package messaging.api.message.controller;

import messaging.api.message.dto.DirectMessageListResponseDto;
import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;
import messaging.api.message.service.DirectMessageService;
import messaging.api.authentication.ExtendedUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class DirectMessageController {
    private final DirectMessageService directMessageService;

    private static final String BASE_REQUEST = "v1/messages";
    public static final String FIND_MESSAGE = BASE_REQUEST + "/{messageId}";
    public static final String FETCH_CONVERSATION = BASE_REQUEST + "/users/{userId}";
    public static final String CREATE_MESSAGE = BASE_REQUEST;
    public static final String UPDATE_MESSAGE = BASE_REQUEST + "/{messageId}";
    public static final String DELETE_MESSAGE = BASE_REQUEST + "/{messageId}";

    @GetMapping(FIND_MESSAGE)
    public DirectMessageResponseDto findMessage(@PathVariable Long messageId, @AuthenticationPrincipal ExtendedUserDetails ud) {
        return directMessageService.findDirectMessage(ud.getId(), messageId);
    }

    @GetMapping(FETCH_CONVERSATION)
    public DirectMessageListResponseDto fetchConversation (@PathVariable Long userId,
                                                           @AuthenticationPrincipal ExtendedUserDetails ud,
                                                           @RequestParam int offset,
                                                           @RequestParam int limit) {
        List<DirectMessageResponseDto> list = directMessageService.fetchConversation(ud.getId(), userId, offset, limit);
        log.info(list.toString());
        return new DirectMessageListResponseDto(list);
    }

    @PostMapping(CREATE_MESSAGE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMessage(@RequestBody DirectMessageRequestDto dto, @AuthenticationPrincipal ExtendedUserDetails ud){
         return directMessageService.createMessage(ud.getId(),dto);
    }

    @PutMapping(UPDATE_MESSAGE)
    public void updateMessage(@PathVariable Long messageId,
                              @RequestBody String value,
                              @AuthenticationPrincipal ExtendedUserDetails ud) {
        directMessageService.updateMessage(ud.getId(), messageId, value);
    }

    @DeleteMapping(DELETE_MESSAGE)
    public void deleteMessage(@PathVariable Long messageId, @AuthenticationPrincipal ExtendedUserDetails ud) {
        directMessageService.deleteMessage(ud.getId(), messageId);
    }
}
