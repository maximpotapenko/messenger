package messenger.user.controller;

import messenger.user.dto.ProfileListResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.dto.UpdateUserRequestDto;
import messenger.user.dto.ProfileResponseDto;
import messenger.authentication.ExtendedUserDetails;
import messenger.user.service.interfaces.UserService;
import messenger.util.exception.ResourceNotFoundException;
import messenger.util.exception.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    private static final String BASE_REQUEST = "/v1/users";
    public static final String FIND_BY_ID = BASE_REQUEST + "/{id}";
    public static final String FIND_USERS_BY_USERNAME = BASE_REQUEST;
    public static final String CREATE_USER = BASE_REQUEST;
    public static final String ADD_ROLE = BASE_REQUEST + "/{id}/roles";
    public static final String REMOVE_ROLE = BASE_REQUEST + "/{id}/roles";
    public static final String UPDATE_ACCOUNT = BASE_REQUEST;
    public static final String BAN_USER = BASE_REQUEST + "/{id}/ban";
    public static final String UNBAN_USER = BASE_REQUEST + "/{id}/unban";
    public static final String DELETE_ACCOUNT = BASE_REQUEST + "/delete-account";
    public static final String RESTORE_ACCOUNT = BASE_REQUEST + "/restore-account";


    @GetMapping(FIND_BY_ID)
    public ResponseEntity<ProfileResponseDto> getUser(@PathVariable Long id) {
        try {
            ProfileResponseDto result = userService.findUser(id);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch(ResourceNotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @GetMapping(FIND_USERS_BY_USERNAME)
    public ProfileListResponseDto findUsersByUsername(@RequestParam String username,
                                                      @RequestParam int offset,
                                                      @RequestParam int limit) {
        return userService.findUsersByUsername(username, offset, limit);
    }

    @PostMapping(CREATE_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody RegistrationRequestDto requestDto) {
        try {
            return userService.createUser(requestDto);
        } catch(UsernameAlreadyExistsException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @PostMapping(ADD_ROLE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addRole(@PathVariable Long id, @RequestParam String name) {
        userService.addRole(id, name);
    }

    @DeleteMapping(REMOVE_ROLE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteRole(@PathVariable Long id, @RequestParam String name) {
        userService.removeRole(id, name);
    }

    @PutMapping(UPDATE_ACCOUNT)
    public void updateUser(@RequestBody UpdateUserRequestDto requestDto, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.updateUser(userDetails.getId(), requestDto);
    }

    @PutMapping(BAN_USER)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void banUser(@PathVariable Long id) {
        userService.banUser(id);
    }

    @PutMapping(UNBAN_USER)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
    }

    @PutMapping(RESTORE_ACCOUNT)
    public void restoreAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.restoreUser(userDetails.getId());

    }

    @DeleteMapping(DELETE_ACCOUNT)
    public void deleteAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.deleteUser(userDetails.getId());
    }
}
