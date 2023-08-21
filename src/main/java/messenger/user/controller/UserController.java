package messenger.user.controller;

import jakarta.validation.Valid;
import messenger.user.dto.*;
import messenger.common.component.security.ExtendedUserDetails;
import messenger.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ProfileResponseDto getUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @GetMapping(FIND_USERS_BY_USERNAME)
    public ProfileListResponseDto findUsersByUsername(@RequestParam String username,
                                                      @RequestParam int offset,
                                                      @RequestParam int limit) {
        return userService.findUsersByUsername(username, offset, limit);
    }

    @PostMapping(CREATE_USER)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody @Valid RegistrationRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @PostMapping(ADD_ROLE)
    @PreAuthorize("hasRole('ADMIN')")
    public void addRole(@PathVariable Long id,
                        @RequestParam String name) {
        userService.addRole(id, name);
    }

    @DeleteMapping(REMOVE_ROLE)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRole(@PathVariable Long id,
                           @RequestParam String name) {
        userService.removeRole(id, name);
    }

    @PutMapping(UPDATE_ACCOUNT)
    public void updateUser(@RequestBody @Valid UpdateUserRequestDto requestDto,
                           @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.updateUser(userDetails.getId(), requestDto);
    }

    @PutMapping(BAN_USER)
    @PreAuthorize("hasRole('ADMIN')")
    public void banUser(@PathVariable Long id) {
        userService.banUser(id);
    }

    @PutMapping(UNBAN_USER)
    @PreAuthorize("hasRole('ADMIN')")
    public void unbanUser(@PathVariable Long id) {
        userService.unbanUser(id);
    }

    @PutMapping(RESTORE_ACCOUNT)
    public void restoreAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails) {
        userService.restoreUser(userDetails.getId());

    }

    @DeleteMapping(DELETE_ACCOUNT)
    public void deleteAccount(@AuthenticationPrincipal ExtendedUserDetails userDetails,
                              @RequestBody @Valid PasswordRequestDto dto) {
        userService.deleteUser(userDetails.getId(), dto.getPassword());
    }
}
