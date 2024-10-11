package az.edu.turing.bankingservice.model.dto.response;

import az.edu.turing.bankingservice.model.enums.AccountStatus;
import az.edu.turing.bankingservice.model.enums.Bank;
import az.edu.turing.bankingservice.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RetrieveAccountResponse(
        Long id,
        String iban,
        String accountNumber,
        Bank bank,
        Currency currency,
        BigDecimal balance,
        AccountStatus status,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:SS")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:SS")
        LocalDateTime updatedAt) {
}
