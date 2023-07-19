package messaging.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
    @Pattern(regexp = "[a-zA-Z]+", message = "Firstname can contain only a-z and A-Z characters")
    @Size(min = 2, max = 20, message ="Firstname must be between 2 and 20 characters long inclusive")
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]+", message = "Lastname can contain only a-z and A-Z characters")
    @Size(min = 2, max = 20, message ="Lastname must be between 2 and 20 characters long inclusive")
    private String lastName;

    @Email
    private String email;
}
