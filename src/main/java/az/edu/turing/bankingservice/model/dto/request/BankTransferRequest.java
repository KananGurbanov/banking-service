package az.edu.turing.bankingservice.model.dto.request;

import az.edu.turing.bankingservice.model.annotations.ValidIban;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record BankTransferRequest(
        @ValidIban
        String toIban,

        @Positive(message = "Transfer amount must be greater than 0")
        BigDecimal amount) {
}
