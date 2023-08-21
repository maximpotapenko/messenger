package messenger.common.component.security;

import messenger.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface ExtendedUserDetails extends UserDetails {

    Long getId();

    User getUser();
}
