package az.edu.turing.bankingservice.exceptions;

public class BadRequestException extends CustomValidationException {
    public BadRequestException(String code, String message) {
        super(code, message);
    }
}
