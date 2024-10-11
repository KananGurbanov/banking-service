package az.edu.turing.bankingservice.controller;

import az.edu.turing.bankingservice.model.dto.response.ExchangeRateResponse;
import az.edu.turing.bankingservice.service.impl.CurrencyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rates")
public class CurrencyController {
    private final CurrencyServiceImpl currencyServiceImpl;

    @GetMapping
    public ResponseEntity<List<ExchangeRateResponse>> getRates() {
        List<ExchangeRateResponse> exchangeRates =
                currencyServiceImpl.getExchangeRates();
        return ResponseEntity.ok(exchangeRates);
    }
}
