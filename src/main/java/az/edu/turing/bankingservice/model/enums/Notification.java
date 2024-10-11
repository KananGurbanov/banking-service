package az.edu.turing.bankingservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Notification {
    ACCOUNT_CREATION("New bank account has been created!","Bank Account Creation"),
    BALANCE_UPDATE("Your balance has been updated! \nAdded amount: {}","Balance Update");

    private final String message;
    private final String subject;

    public String formatMessage(Object... args) {
        return String.format(message, args);
    }

}
