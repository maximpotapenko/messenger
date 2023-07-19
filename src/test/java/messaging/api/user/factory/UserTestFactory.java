package messaging.api.user.factory;

import messaging.api.user.dto.RegistrationRequestDto;
import messaging.api.user.entity.User;

public interface UserTestFactory {
    User getUser();

    RegistrationRequestDto getRegistrationRequestDto();
}
