package az.edu.turing.bankingservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ExchangeRateResponse(
        String currencyCode,

        BigDecimal rate,

        @JsonFormat(pattern = "dd-MM-yyyy HH:ss")
        LocalDateTime lastUpdated) {
}
