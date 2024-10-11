package az.edu.turing.bankingservice.model.dto.request;

import az.edu.turing.bankingservice.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(
        @NotBlank(message = "Name must not be empty")
        String name,

        @NotBlank(message = "Surname must not be empty")
        String surname,

        @Email(message = "Email is not valid")
        String email,

        @NotNull
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "The password must be at least 8 characters long " +
                        "and can contain any of the specified character sets")
        String password,

        Role role) {
}
