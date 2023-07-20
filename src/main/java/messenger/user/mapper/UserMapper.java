package messenger.user.mapper;

import messenger.user.dto.ProfileResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;
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
    default String encoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}