package az.edu.turing.bankingservice.model.dto;

import lombok.Builder;

@Builder
public record RestResponse<T>(
        String status,
        T data) {
}
