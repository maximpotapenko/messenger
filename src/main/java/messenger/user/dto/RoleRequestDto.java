package messenger.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.user.constraint.ValidRoleName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequestDto {

    @ValidRoleName
    private String name;
}
