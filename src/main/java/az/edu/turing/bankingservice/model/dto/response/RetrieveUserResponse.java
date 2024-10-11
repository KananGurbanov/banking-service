package az.edu.turing.bankingservice.model.dto.response;

import az.edu.turing.bankingservice.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RetrieveUserResponse(
        Long id,
        String name,
        String surname,
        String email,
        String password,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt,
        Role role){
}
