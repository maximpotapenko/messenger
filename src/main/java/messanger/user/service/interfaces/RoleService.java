package messanger.user.service.interfaces;

import messanger.user.entity.Role;
import messanger.user.dto.RoleRequestDto;
import messanger.user.dto.RoleResponseDto;

import java.util.List;

public interface RoleService {

    Long createRole(RoleRequestDto dto);

    RoleResponseDto findRoleByName(String name);

    Role findRoleEntityByName(String name);

    void deleteRoleByName(String name);

    List<RoleResponseDto> findAllRoles();
}
