package messenger.user.service;

import messenger.user.entity.Role;
import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;

import java.util.List;

public interface RoleService {

    Long createRole(RoleRequestDto dto);

    RoleResponseDto findRoleByName(String name);

    Role findRoleEntityByName(String name);

    void deleteRoleByName(String name);

    List<RoleResponseDto> findAllRoles();
}
