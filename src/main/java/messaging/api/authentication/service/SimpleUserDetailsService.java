package messaging.api.authentication.service;

import messaging.api.authentication.UserDetailsImpl;
import messaging.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(repository
                                    .findByUsername(username)
                                    .orElseThrow( () -> new UsernameNotFoundException("Couldn't find user with username: " + username) ));
    }
}
