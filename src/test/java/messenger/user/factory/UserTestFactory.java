package messenger.user.factory;

import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;

public interface UserTestFactory {
    User getUser();

    RegistrationRequestDto getRegistrationRequestDto();
}
