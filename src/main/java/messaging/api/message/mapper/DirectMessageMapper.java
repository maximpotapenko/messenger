package messaging.api.message.mapper;

import messaging.api.message.entity.DirectMessage;
import messaging.api.message.dto.DirectMessageRequestDto;
import messaging.api.message.dto.DirectMessageResponseDto;
import messaging.api.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DirectMessageMapper {

    DirectMessageMapper INSTANCE = Mappers.getMapper(DirectMessageMapper.class);

    @Mapping(target = "author", source = "authorId", qualifiedByName = "idToUserEntity")
    @Mapping(target = "recipient", source = "recipientId", qualifiedByName = "idToUserEntity")
    DirectMessage toEntity(DirectMessageRequestDto directMessageRequestDto);

    @Mapping(target = "authorId", source = "author", qualifiedByName = "userEntityToId")
    @Mapping(target = "recipientId", source = "recipient", qualifiedByName = "userEntityToId")
    @Mapping(target = "authorName", source = "author", qualifiedByName = "authorToAuthorName")
    DirectMessageResponseDto toDto(DirectMessage directMessage/*, @Context TimeZone timeZone*/);

    @Named("idToUserEntity")
    static User idToUserEntity(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
    @Named("userEntityToId")
    static Long userEntityToId(User user) {
        return user.getId();
    }

    @Named("authorToAuthorName")
    default String toAuthorName(User user) {
        return user.getUsername();
    }
}
