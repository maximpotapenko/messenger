package messaging.api.user.controller;

import messaging.api.user.dto.RegistrationRequestDto;
import messaging.api.user.dto.UpdateUserRequestDto;
import messaging.api.user.dto.ProfileResponseDto;
import messaging.api.authentication.ExtendedUserDetails;
import messaging.api.user.service.interfaces.UserService;
import messaging.api.util.exception.ResourceNotFoundException;
import messaging.api.util.exception.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("v1/users/")
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<ProfileResponseDto> getUser(@PathVariable Long id) {
        try {
            ProfileResponseDto result = userService.findUser(id);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch(ResourceNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfileResponseDto>> findUsersByUsername(@RequestParam String q, @RequestParam int offset, @RequestParam int limit) {
        List<ProfileResponseDto> list = userService.findUsersByUsername(q, offset, limit);

        return new ResponseEntity<>(list, HttpStatusCode.valueOf(200));
    }

    @PostMapping
    public ResponseEntity<Object> createUser(RegistrationRequestDto requestDto) {
        try {
            userService.createUser(requestDto);
            return new ResponseEntity<>(HttpStatusCode.valueOf(201));
        } catch(UsernameAlreadyExistsException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/{id}/roles/")
    public void addRole(@PathVariable Long id, @RequestParam String name) {
        userService.addRole(id, name);
    }

    @DeleteMapping("/{id}/roles/")
    public void deleteRole(@PathVariable Long id, @RequestParam String name) {
        userService.removeRole(id, name);
    }

    @PutMapping("/update-account")
    public void updateUser(@RequestBody UpdateUserRequestDto requestDto, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.updateUser(userDetails.getId(), requestDto);
    }

    @PutMapping("/{id}/ban")
    public void banUser(@PathVariable Long id) {
        userService.banUser(id);
    }

    @PutMapping("/{id}/unban")
    public void unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
    }

    @PutMapping("/restore-account")
    public void restoreUser(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.restoreUser(userDetails.getId());

    }

    @DeleteMapping("/delete-account")
    public void deleteUser(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.deleteUser(userDetails.getId());
    }
}
