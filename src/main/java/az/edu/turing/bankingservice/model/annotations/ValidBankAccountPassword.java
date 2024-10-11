package az.edu.turing.bankingservice.model.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{10,}$", 
    message = "Password must be at least 10 characters long, " +
            "contain at least one uppercase letter, one lowercase letter," +
            " one number, one special character, and no spaces."
)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidBankAccountPassword {

    String message() default "Invalid password format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
