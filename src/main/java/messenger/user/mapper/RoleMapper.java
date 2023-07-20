package messenger.user.mapper;

import messenger.user.entity.Role;
import messenger.user.dto.RoleRequestDto;
import messenger.user.dto.RoleResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleRequestDto dto);

    RoleResponseDto toDto(Role entity);

}
