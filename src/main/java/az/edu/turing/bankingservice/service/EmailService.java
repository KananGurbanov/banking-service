package az.edu.turing.bankingservice.service;

public interface EmailService {
    void sendEmail(String email, String message, String subject);
}
