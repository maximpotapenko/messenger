package messaging.api.message.controller;

import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;
import messaging.api.message.service.DirectMessageService;
import messaging.api.authentication.ExtendedUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("v1/messages/")
public class DirectMessageController {

    private final DirectMessageService directMessageService;

    @GetMapping("/{messageId}")
    public DirectMessageResponseDto findMessage(@PathVariable Long messageId) {
        return directMessageService.findDirectMessage(messageId);
    }

    @GetMapping("/users/{userId}")
    public List<DirectMessageResponseDto> fetchConversation (@PathVariable Long userId,
                                                             @AuthenticationPrincipal ExtendedUserDetails ud,
                                                             @RequestParam int offset,
                                                             @RequestParam int limit) {
        return directMessageService.fetchConversation(ud.getId(), userId, offset, limit);
    }

    @PostMapping
    public void createMessage(DirectMessageRequestDto dto){
        directMessageService.createMessage(dto);
    }

    @PutMapping("/{messageId}")
    public void updateMessage(@PathVariable Long messageId, @RequestBody String value) {
        directMessageService.updateMessage(messageId, value);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {
        directMessageService.deleteMessage(messageId);
    }
}
