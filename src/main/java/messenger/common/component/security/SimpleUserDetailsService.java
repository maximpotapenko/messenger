package messenger.common.component.security;

import lombok.extern.slf4j.Slf4j;
import messenger.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SimpleUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> {
                    log.info("Authentication exception: User {} does not exist", username);
                    return new UsernameNotFoundException("User " + username + " does not exist");
                });
    }
}
