package messenger.common.component.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
