package messenger.user.mapper;

import messenger.user.dto.ProfileResponseDto;
import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    @Mapping(target ="password", source ="password", qualifiedByName = "encodePassword")
    public abstract User toEntity(RegistrationRequestDto dto);

    public abstract ProfileResponseDto toProfileResponseDto(User user);

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return encoder.encode(password);
    }
}
