package messenger.message.mapper;

import messenger.message.entity.DirectMessage;
import messenger.message.dto.DirectMessageRequestDto;
import messenger.message.dto.DirectMessageResponseDto;
import messenger.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DirectMessageMapper {

    @Mapping(target = "recipient", source = "recipientId", qualifiedByName = "idToUserEntity")
    DirectMessage toEntity(DirectMessageRequestDto directMessageRequestDto);

    @Mapping(target = "authorId", source = "author", qualifiedByName = "userEntityToId")
    @Mapping(target = "recipientId", source = "recipient", qualifiedByName = "userEntityToId")
    DirectMessageResponseDto toDto(DirectMessage directMessage/*, @Context TimeZone timeZone*/);

    @Named("idToUserEntity")
    default User idToUserEntity(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
    @Named("userEntityToId")
    default Long userEntityToId(User user) {
        return user.getId();
    }

}
