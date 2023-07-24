package messenger.user.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = {})
@Pattern(regexp = "\\w+", message = "Username can contain only a-z, A-Z, 0-9 and _ characters")
@Pattern(regexp = ".*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*", message = "Username must contain at least 4 letters")
@Pattern(regexp = "^\\D.*", message = "Username cannot start with a digit")
@Size(min = 4, max = 18, message ="Username must be between 4 and 18 characters long inclusive")
public @interface ValidUsername {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
