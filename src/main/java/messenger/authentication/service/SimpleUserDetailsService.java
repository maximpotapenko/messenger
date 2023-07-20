package messenger.authentication.service;

import messenger.authentication.UserDetailsImpl;
import messenger.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import messenger.util.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }
}
