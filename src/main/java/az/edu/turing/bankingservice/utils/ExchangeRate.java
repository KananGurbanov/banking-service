package az.edu.turing.bankingservice.utils;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "EXCHANGE_RATES")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", nullable = false, length = 3, unique = true)
    private String currencyCode;

    @Column(name = "rate", nullable = false, precision = 18, scale = 6)
    private BigDecimal rate;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
