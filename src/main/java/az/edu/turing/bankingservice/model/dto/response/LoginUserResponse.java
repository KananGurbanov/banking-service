package az.edu.turing.bankingservice.model.dto.response;

import lombok.Builder;

@Builder
public record LoginUserResponse(
        String accessToken,
        String refreshToken) {
}
