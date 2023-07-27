package messenger.user.service;

import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;
import messenger.user.mapper.RoleMapper;
import messenger.user.repository.RoleRepository;
import messenger.user.entity.Role;
import messenger.exception.ResourceAlreadyExistsException;
import messenger.exception.ResourceNotFoundException;
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
    public Long createRole(RoleRequestDto dto) {
        Role r = roleMapper.toEntity(dto);

        if(roleRepository.existsRoleByName(dto.getName())) throw new ResourceAlreadyExistsException("Role already exists");

        roleRepository.saveAndFlush(r);

        log.info("Added new role {} to database ", r);

        return r.getId();
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
