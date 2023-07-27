package messenger.user.factory;

import messenger.user.dto.RegistrationRequestDto;
import messenger.user.entity.User;

public interface UserTestFactory {

    User getAdmin();

    User getUser();

    RegistrationRequestDto getRegistrationRequestDto();

    String getRawPassword();
}
