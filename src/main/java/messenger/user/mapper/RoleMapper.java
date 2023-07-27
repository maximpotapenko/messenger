package messenger.user.mapper;

import messenger.user.entity.Role;
import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    Role toEntity(RoleRequestDto dto);

    RoleResponseDto toDto(Role entity);

}
