package messenger.user.service;

import messenger.user.dto.ProfileListResponseDto;
import messenger.user.entity.User;
import messenger.user.mapper.UserMapper;
import messenger.user.entity.Role;
import messenger.user.dto.ProfileResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.dto.UpdateUserRequestDto;
import messenger.user.repository.UserRepository;
import messenger.common.exception.ResourceAlreadyExistsException;
import messenger.common.exception.ResourceNotFoundException;
import messenger.common.exception.UsernameAlreadyExistsException;
import messenger.common.exception.WrongPasswordException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserMapper userMapper;

    @Override
    public Long createUser(RegistrationRequestDto registrationRequestDto) {
        User user = userMapper.toEntity(registrationRequestDto);

        if(userRepository.existsUserEntityByUsername(user.getUsername()))
            throw new UsernameAlreadyExistsException("Username " + user.getUsername() + " already exists");

        userRepository.saveAndFlush(user);

        log.info("User {} added to database", user.getId());

        return user.getId();
    }

    @Override
    public ProfileResponseDto findUser(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::toProfileResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Override
    public ProfileListResponseDto findUsersByUsername(String username, int offset, int limit) {
        if(limit > 50) throw new IllegalArgumentException("limit cannot be greater than 50");

        List<ProfileResponseDto> list = userRepository.findClosestUsersByUsernameLike(username, offset, limit)
                .stream()
                .map(userMapper::toProfileResponseDto)
                .toList();

        return new ProfileListResponseDto(list);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new WrongPasswordException("Wrong password");

        user.setPassword(newPassword);

        userRepository.saveAndFlush(user);

        log.info("User {} changed password", userId);
    }

    @Override
    public void changeUsername(Long userId, String newUsername){
        if(userRepository.existsUserEntityByUsername(newUsername))
            throw new UsernameAlreadyExistsException("Username " + newUsername + " already exists");

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        String oldUsername = user.getUsername();

        user.setUsername(newUsername);

        userRepository.saveAndFlush(user);

        log.info("User {} changed username from {} to {}", userId, oldUsername, newUsername);
    }

    @Override
    public void updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        if(updateUserRequestDto.getFirstName() != null)
            user.setFirstName(updateUserRequestDto.getFirstName());
        if(updateUserRequestDto.getLastName() != null)
            user.setLastName(updateUserRequestDto.getLastName());
        if(updateUserRequestDto.getEmail() != null)
            user.setEmail(updateUserRequestDto.getEmail());

        userRepository.saveAndFlush(user);

        log.info("User {} updated info", userId);
    }

    @Override
    public void addRole(Long userId, String role) {
        Role r = roleService.findRoleEntityByName(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        List<Role> roles = user.getRoles();

        if(roles.contains(r))
            throw new ResourceAlreadyExistsException("User already has role " + r.getName());

        roles.add(r);

        userRepository.saveAndFlush(user);

        log.info("Added role {} to User {}", role, userId);
    }

    @Override
    public void removeRole(Long userId, String role) {
        Role r = roleService.findRoleEntityByName(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        user.getRoles().remove(r);

        userRepository.saveAndFlush(user);

        log.info("Removed role {} from User {}", role, userId);
    }

    @Override
    public void deleteUser(Long id, String password) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new WrongPasswordException("Wrong password");

        user.setDeleted(true);

        userRepository.saveAndFlush(user);

        log.info("User {} has been marked as deleted", id);
    }

    @Override
    public void restoreUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setDeleted(false);

        userRepository.saveAndFlush(user);

        log.info("User {} has been marked as NOT deleted", id);
    }

    @Override
    public void banUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setBanned(true);

        userRepository.saveAndFlush(user);

        log.info("User {} has been banned", id);
    }

    @Override
    public void unbanUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setBanned(false);

        userRepository.saveAndFlush(user);

        log.info("User {} has been unbanned", id);
    }
}
