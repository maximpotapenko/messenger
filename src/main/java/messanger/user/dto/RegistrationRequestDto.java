package messanger.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {
    @Pattern(regexp = "\\w+", message = "Username can contain only a-z, A-Z, 1-9 and _ characters")
    @Pattern(regexp = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*", message = "Username must contain at least 4 letters")
    @Pattern(regexp = "^\\D.*", message = "Username cannot start with a digit")
    @Size(min = 4, max = 18, message ="Username must be between 4 and 18 characters long inclusive")
    private String username;

    @Pattern(regexp = "\\w+", message = "Password can contain only a-z, A-Z, 1-9 and _ characters")
    @Pattern(regexp = "^[^_]*(_[^_]*){0,3}$", message = "Password cannot contain more than 3 \"_\" characters ")
    @Size(min = 8, max = 24, message ="Password must be between 8 and 24 characters long inclusive")
    private String password;

    @Pattern(regexp = "[a-zA-Z]+", message = "Firstname can contain only a-z and A-Z characters")
    @Size(min = 2, max = 20, message ="Firstname must be between 2 and 20 characters long inclusive")
    private String firstName;

    @Pattern(regexp = "[a-zA-Z]+", message = "Lastname can contain only a-z and A-Z characters")
    @Size(min = 2, max = 20, message ="Lastname must be between 2 and 20 characters long inclusive")
    private String lastName;

    @Email
    private String email;
}
