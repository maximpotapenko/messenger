package messanger.user.service;

import messanger.user.dto.ProfileListResponseDto;
import messanger.user.entity.User;
import messanger.user.mapper.UserMapper;
import messanger.user.entity.Role;
import messanger.user.dto.ProfileResponseDto;
import messanger.user.dto.RegistrationRequestDto;
import messanger.user.dto.UpdateUserRequestDto;
import messanger.user.repository.UserRepository;
import messanger.user.service.interfaces.RoleService;
import messanger.user.service.interfaces.UserService;
import messanger.util.exception.ResourceAlreadyExistsException;
import messanger.util.exception.ResourceNotFoundException;
import messanger.util.exception.UsernameAlreadyExistsException;
import messanger.util.exception.WrongPasswordException;
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

        log.info("{} added to the database", user);

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
    public ProfileResponseDto findUser(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::toProfileResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException(username));
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

        log.info("{} username changed from " + oldPassword + " to " + newPassword, user);
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

        log.info("{} username changed from " + oldUsername + " to " + user.getUsername(), user);
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

        log.info("Added role " + r.getName() + " to user with id: " + user.getId());
    }

    @Override
    public void removeRole(Long userId, String role) {
        Role r = roleService.findRoleEntityByName(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId));

        user.getRoles().remove(r);

        userRepository.saveAndFlush(user);

        log.info("Removed role " + r.getName() + " from user with id: " + user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setDeleted(true);

        userRepository.saveAndFlush(user);

        log.info("{} has been marked as deleted", user);
    }

    @Override
    public void restoreUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setDeleted(false);

        userRepository.saveAndFlush(user);

        log.info("{} has been marked as NOT deleted", user);
    }

    @Override
    public void banUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setBanned(true);

        userRepository.saveAndFlush(user);

        log.info("Banned {}", user);
    }

    @Override
    public void unbanUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        user.setBanned(false);

        userRepository.saveAndFlush(user);

        log.info("Unbanned {}", user);
    }
}
