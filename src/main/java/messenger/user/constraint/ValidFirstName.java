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
@Pattern(regexp = "[a-zA-Z]+", message = "Firstname can contain only a-z and A-Z characters")
@Size(min = 2, max = 20, message ="Firstname must be between 2 and 20 characters long inclusive")
public @interface ValidFirstName {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
