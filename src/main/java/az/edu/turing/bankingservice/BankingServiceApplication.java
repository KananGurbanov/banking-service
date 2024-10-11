package az.edu.turing.bankingservice;

import az.edu.turing.bankingservice.service.EmailService;
import az.edu.turing.bankingservice.service.impl.EmailServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class BankingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingServiceApplication.class, args);
    }
}
