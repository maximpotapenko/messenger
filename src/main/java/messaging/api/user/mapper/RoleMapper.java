package messaging.api.user.mapper;

import messaging.api.user.entity.Role;
import messaging.api.user.dto.RoleRequestDto;
import messaging.api.user.dto.RoleResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleRequestDto dto);

    RoleResponseDto toDto(Role entity);

}
