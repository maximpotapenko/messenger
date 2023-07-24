package messenger.websocket.principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StompPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return name;
    }
}
