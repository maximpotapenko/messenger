package messenger.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
