package messaging.api.authentication.service;

import messaging.api.authentication.ExtendedUserDetails;
import messaging.api.authentication.UserDetailsImpl;
import messaging.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import messaging.api.util.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }
}
