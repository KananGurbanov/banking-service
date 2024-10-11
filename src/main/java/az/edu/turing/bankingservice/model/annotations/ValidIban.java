package az.edu.turing.bankingservice.model.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(
        regexp = "^AZ\\d{2}[A-Z]{3,4}\\d{20}$\n",
        message = "Iban should be valid"
)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidIban {

    String message() default "Invalid iban format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
