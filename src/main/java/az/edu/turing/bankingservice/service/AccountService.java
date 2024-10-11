package az.edu.turing.bankingservice.service;

import az.edu.turing.bankingservice.dao.entity.AccountEntity;
import az.edu.turing.bankingservice.model.dto.response.RetrieveAccountResponse;
import az.edu.turing.bankingservice.model.dto.request.CreateAccountRequest;

import java.util.List;

public interface AccountService {
    void createAccount(Long userId, CreateAccountRequest request);

    void deleteAccount(Long userId, Long accountId);

    List<RetrieveAccountResponse> getAccounts(Long userId);

    RetrieveAccountResponse getAccount(Long userId, Long accountId);

    AccountEntity getActiveAccount(Long userId, Long accountId);

    AccountEntity getAccountEntityByIban(String iban);
}
