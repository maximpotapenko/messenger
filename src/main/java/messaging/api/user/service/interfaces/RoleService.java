package messaging.api.user.service.interfaces;


import messaging.api.user.entity.Role;
import messaging.api.user.dto.RoleRequestDto;
import messaging.api.user.dto.RoleResponseDto;

import java.util.List;

public interface RoleService {

    RoleResponseDto createRole(RoleRequestDto dto);

    RoleResponseDto findRoleByName(String name);

    Role findRoleEntityByName(String name);

    void deleteRoleByName(String name);

    List<RoleResponseDto> findAllRoles();
}
