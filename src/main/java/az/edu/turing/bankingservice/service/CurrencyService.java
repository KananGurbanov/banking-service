package az.edu.turing.bankingservice.service;

import az.edu.turing.bankingservice.model.dto.response.ExchangeRateResponse;

import java.util.List;

public interface CurrencyService {
    List<ExchangeRateResponse> getExchangeRates();
}
