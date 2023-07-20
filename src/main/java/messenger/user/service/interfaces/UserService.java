package messenger.user.service.interfaces;

import messenger.user.dto.ProfileListResponseDto;
import messenger.user.dto.ProfileResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.dto.UpdateUserRequestDto;

public interface UserService {

    Long createUser(RegistrationRequestDto registrationRequestDto);

    ProfileResponseDto findUser(Long id);

    ProfileResponseDto findUser(String username);

    ProfileListResponseDto findUsersByUsername(String username, int offset, int limit);

    void changePassword(Long userId, String oldPassword, String newPassword);

    void changeUsername(Long userId, String newUsername);

    void updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

    void addRole(Long userId, String role);

    void removeRole(Long userId, String role);

    void deleteUser(Long id);

    void restoreUser(Long id);

    void banUser(Long id);

    void unbanUser(Long id);
}
