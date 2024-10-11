package az.edu.turing.bankingservice.service;

import az.edu.turing.bankingservice.model.dto.request.BankTransferRequest;
import az.edu.turing.bankingservice.model.dto.request.TopUpRequest;

public interface TransferService {
    void makeBankTransfer(Long userId, Long accountId, BankTransferRequest transferDto);
    void topUp(Long userId, Long accountId, TopUpRequest request);
}
