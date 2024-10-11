package az.edu.turing.bankingservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginUserRequest(
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password must not be empty")
        String password) {
}
