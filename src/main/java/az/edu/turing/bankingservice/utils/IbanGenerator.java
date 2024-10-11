package az.edu.turing.bankingservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IbanGenerator {
    static final String countryCode = "AZ";

    public static String generateIban(String bankCode, String accountNumber) {

        String ibanWithoutCheckDigits = countryCode + bankCode + accountNumber;

        String checkDigits = calculateCheckDigits(ibanWithoutCheckDigits);

        return countryCode + checkDigits + bankCode + accountNumber;
    }



    public static String getAccountNumber() {
        SecureRandom random = new SecureRandom();

        StringBuilder accountNumber = new StringBuilder(20);

        accountNumber.append(random.nextInt(9) + 1);

        for (int i = 0; i < 19; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }


    private static String calculateCheckDigits(String ibanWithoutCheckDigits) {
        String rearrangedIban = ibanWithoutCheckDigits.substring(4) + countryCode + "00";

        StringBuilder numericIban = new StringBuilder();
        for (char c : rearrangedIban.toCharArray()) {
            if (Character.isDigit(c)) {
                numericIban.append(c);
            } else {
                numericIban.append(c - 'A' + 10);
            }
        }

        BigInteger ibanBigInt = new BigInteger(numericIban.toString());
        int mod97 = ibanBigInt.mod(BigInteger.valueOf(97)).intValue();

        int checkDigits = 98 - mod97;

        return String.format("%02d", checkDigits);
    }
}
