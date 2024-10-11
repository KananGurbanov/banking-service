package az.edu.turing.bankingservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Bank {
    ABB("IBA"),
    KAPITAL_BANK("KAPB"),
    PASHA_BANK("PASB"),
    UNI_BANK("UNIB"),
    EXPRESS_BANK("EXB"),
    BANK_OF_BAKU("EXB");

    private final String bankCode;
}
