package az.edu.turing.bankingservice.service.impl;


import az.edu.turing.bankingservice.model.dto.response.ExchangeRateResponse;
import az.edu.turing.bankingservice.service.CurrencyService;
import az.edu.turing.bankingservice.utils.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final ExchangeRateRepository exchangeRateRepository;

    public List<ExchangeRateResponse> getExchangeRates() {
        return exchangeRateRepository.findAll().stream().map(exchangeRate -> ExchangeRateResponse.builder()
                .rate(exchangeRate.getRate())
                .currencyCode(exchangeRate.getCurrencyCode())
                .lastUpdated(exchangeRate.getLastUpdated())
                .build()).toList();
    }
}
