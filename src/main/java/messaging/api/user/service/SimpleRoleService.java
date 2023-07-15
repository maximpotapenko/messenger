package messaging.api.user.service;

import messaging.api.user.dto.RoleRequestDto;
import messaging.api.user.dto.RoleResponseDto;
import messaging.api.user.mapper.RoleMapper;
import messaging.api.user.repository.RoleRepository;
import messaging.api.user.service.interfaces.RoleService;
import messaging.api.user.entity.Role;
import messaging.api.util.exception.ResourceAlreadyExistsException;
import messaging.api.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SimpleRoleService implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public RoleResponseDto createRole(RoleRequestDto dto) {
        Role r = roleMapper.toEntity(dto);

        if(roleRepository.existsRoleByName(dto.getName())) throw new ResourceAlreadyExistsException("Role already exists");

        roleRepository.saveAndFlush(r);

        log.info("Added new role {} to database ", r);

        return roleMapper.toDto(r);
    }

    @Override
    public RoleResponseDto findRoleByName(String name) {
        Role r = findRoleEntityByName(name);

        return roleMapper.toDto(r);
    }

    @Override
    public Role findRoleEntityByName(String name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(name));
    }

    @Override
    public void deleteRoleByName(String name) {
        Role r = roleRepository.findRoleByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(name));

        roleRepository.delete(r);

        log.info("Deleted role {} from database", r);
    }

    @Override
    public List<RoleResponseDto> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }
}
