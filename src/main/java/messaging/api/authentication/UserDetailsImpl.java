package messaging.api.authentication;

import messaging.api.user.entity.Role;
import messaging.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements ExtendedUserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<CustomGrantedAuthority> list = new ArrayList<>();

        for(Role i : user.getRoles()) {
            list.add(new CustomGrantedAuthority(i.getName()));
        }

        return list;
    }
    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getUser().isBanned();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
