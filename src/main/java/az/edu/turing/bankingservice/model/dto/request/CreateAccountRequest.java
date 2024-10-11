package az.edu.turing.bankingservice.model.dto.request;

import az.edu.turing.bankingservice.model.annotations.ValidBankAccountPassword;
import az.edu.turing.bankingservice.model.enums.Bank;
import az.edu.turing.bankingservice.model.enums.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest (
        @NotNull
        Bank bank,

        @ValidBankAccountPassword
        String password,

        @NotNull
        Currency currency) {
}
