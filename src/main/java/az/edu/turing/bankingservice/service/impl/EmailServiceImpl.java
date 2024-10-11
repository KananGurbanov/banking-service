package az.edu.turing.bankingservice.service.impl;

import az.edu.turing.bankingservice.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @SneakyThrows
    @Override
    public void sendEmail(String to, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        mailSender.send(mimeMessage);
    }
}
