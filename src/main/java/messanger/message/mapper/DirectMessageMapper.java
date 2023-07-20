package messanger.message.mapper;

import messanger.message.entity.DirectMessage;
import messanger.message.dto.DirectMessageRequestDto;
import messanger.message.dto.DirectMessageResponseDto;
import messanger.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DirectMessageMapper {
    DirectMessageMapper INSTANCE = Mappers.getMapper(DirectMessageMapper.class);

    @Mapping(target = "recipient", source = "recipientId", qualifiedByName = "idToUserEntity")
    DirectMessage toEntity(DirectMessageRequestDto directMessageRequestDto);

    @Mapping(target = "authorId", source = "author", qualifiedByName = "userEntityToId")
    @Mapping(target = "recipientId", source = "recipient", qualifiedByName = "userEntityToId")
    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToAuthorName")
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

    @Named("authorToAuthorName")
    default String toAuthorName(User user) {
        return user.getUsername();
    }
}
