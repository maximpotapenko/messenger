package messaging.api.user.service.interfaces;

import messaging.api.user.dto.ProfileResponseDto;
import messaging.api.user.dto.RegistrationRequestDto;
import messaging.api.user.dto.UpdateUserRequestDto;

import java.util.List;

public interface UserService {

    void createUser(RegistrationRequestDto registrationRequestDto);

    ProfileResponseDto findUser(Long id);

    ProfileResponseDto findUser(String username);

    List<ProfileResponseDto> findUsersByUsername(String username, int offset, int limit);

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
