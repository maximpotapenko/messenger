package messaging.api.user.mapper;

import messaging.api.user.dto.ProfileResponseDto;
import messaging.api.user.dto.RegistrationRequestDto;
import messaging.api.user.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target ="password", source ="password", qualifiedByName = "encoder")
    User toEntity(RegistrationRequestDto dto);

    ProfileResponseDto toProfileResponseDto(User user);

    @Named("encoder")
    default String encode(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
