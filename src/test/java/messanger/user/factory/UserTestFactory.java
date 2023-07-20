package messanger.user.factory;

import messanger.user.dto.RegistrationRequestDto;
import messanger.user.entity.User;

public interface UserTestFactory {
    User getUser();

    RegistrationRequestDto getRegistrationRequestDto();
}
