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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("v1/users/{id}")
    public ResponseEntity<ProfileResponseDto> getUser(@PathVariable Long id) {
        try {
            ProfileResponseDto result = userService.findUser(id);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch(ResourceNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @GetMapping("v1/users")
    public ResponseEntity<List<ProfileResponseDto>> findUsersByUsername(@RequestParam String username, @RequestParam int offset, @RequestParam int limit) {
        List<ProfileResponseDto> list = userService.findUsersByUsername(username, offset, limit);

        return new ResponseEntity<>(list, HttpStatusCode.valueOf(200));
    }

    @PostMapping("v1/users")
    public ResponseEntity<Object> createUser(@RequestBody RegistrationRequestDto requestDto) {
        try {
            userService.createUser(requestDto);
            return new ResponseEntity<>(HttpStatusCode.valueOf(201));
        } catch(UsernameAlreadyExistsException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @PostMapping("v1/users/{id}/roles/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addRole(@PathVariable Long id, @RequestParam String name) {
        userService.addRole(id, name);
    }

    @DeleteMapping("v1/users/{id}/roles/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteRole(@PathVariable Long id, @RequestParam String name) {
        userService.removeRole(id, name);
    }

    @PutMapping("v1/users/update-account")
    public void updateUser(@RequestBody UpdateUserRequestDto requestDto, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.updateUser(userDetails.getId(), requestDto);
    }

    @PutMapping("v1/users/{id}/ban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void banUser(@PathVariable Long id) {
        userService.banUser(id);
    }

    @PutMapping("v1/users/{id}/unban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
    }

    @PutMapping("v1/users/restore-account")
    public void restoreAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.restoreUser(userDetails.getId());

    }

    @DeleteMapping("v1/users/delete-account")
    public void deleteAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.deleteUser(userDetails.getId());
    }
}
