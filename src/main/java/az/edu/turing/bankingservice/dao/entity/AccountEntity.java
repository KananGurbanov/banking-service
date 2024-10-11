package az.edu.turing.bankingservice.dao.entity;

import az.edu.turing.bankingservice.model.enums.AccountStatus;
import az.edu.turing.bankingservice.model.enums.Bank;
import az.edu.turing.bankingservice.model.enums.Currency;
import az.edu.turing.bankingservice.utils.IbanGenerator;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "ACCOUNTS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Bank bank;

    String accountNumber;

    String iban;

    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    Currency currency;

    String password;

    @Enumerated(EnumType.STRING)
    AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Version
    Long version;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @PrePersist
    private void defaultValues() {

        if (this.accountNumber == null) {
            this.accountNumber = IbanGenerator.getAccountNumber();
        }

        if (this.iban == null) {
            this.iban = IbanGenerator.generateIban(bank.getBankCode(), accountNumber);
        }
    }
}
