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
@Pattern(regexp = "\\w+", message = "Password can contain only a-z, A-Z, 1-9 and _ characters")
@Pattern(regexp = "^[^_]*(_[^_]*){0,3}$", message = "Password cannot contain more than 3 \"_\" characters ")
@Size(min = 8, max = 24, message ="Password must be between 8 and 24 characters long inclusive")
public @interface ValidPassword {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
