package messenger.message.controller;

import jakarta.validation.Valid;
import messenger.message.dto.DirectMessageListResponseDto;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;
import messenger.message.dto.UpdateMessageRequestDto;
import messenger.message.service.DirectMessageService;
import messenger.security.ExtendedUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public DirectMessageResponseDto findMessage(@PathVariable Long messageId,
                                                @AuthenticationPrincipal ExtendedUserDetails ud) {
        return directMessageService.findDirectMessage(ud.getId(), messageId);
    }

    @GetMapping(FETCH_CONVERSATION)
    public DirectMessageListResponseDto fetchConversation (@PathVariable Long userId,
                                                           @AuthenticationPrincipal ExtendedUserDetails ud,
                                                           @RequestParam int offset,
                                                           @RequestParam int limit) {
        return directMessageService.fetchConversation(ud.getId(), userId, offset, limit);
    }

    @PostMapping(CREATE_MESSAGE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createMessage(@RequestBody @Valid DirectMessageRequestDto dto,
                              @AuthenticationPrincipal ExtendedUserDetails ud){
         return directMessageService.createMessage(ud.getId(),dto);
    }

    @PutMapping(UPDATE_MESSAGE)
    public void updateMessage(@PathVariable Long messageId,
                              @RequestBody @Valid UpdateMessageRequestDto dto,
                              @AuthenticationPrincipal ExtendedUserDetails ud) {
        directMessageService.updateMessage(ud.getId(), messageId, dto.getMessage());
    }

    @DeleteMapping(DELETE_MESSAGE)
    public void deleteMessage(@PathVariable Long messageId,
                              @AuthenticationPrincipal ExtendedUserDetails ud) {
        directMessageService.deleteMessage(ud.getId(), messageId);
    }
}
