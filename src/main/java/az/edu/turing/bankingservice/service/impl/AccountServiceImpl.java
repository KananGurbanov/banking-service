package az.edu.turing.bankingservice.service.impl;

import az.edu.turing.bankingservice.dao.entity.AccountEntity;
import az.edu.turing.bankingservice.dao.entity.UserEntity;
import az.edu.turing.bankingservice.dao.repository.AccountRepository;
import az.edu.turing.bankingservice.dao.repository.UserRepository;
import az.edu.turing.bankingservice.exceptions.BadRequestException;
import az.edu.turing.bankingservice.exceptions.NotFoundException;
import az.edu.turing.bankingservice.mapper.AccountMapper;
import az.edu.turing.bankingservice.model.dto.response.RetrieveAccountResponse;
import az.edu.turing.bankingservice.model.dto.request.CreateAccountRequest;
import az.edu.turing.bankingservice.model.enums.AccountStatus;
import az.edu.turing.bankingservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static az.edu.turing.bankingservice.model.enums.Error.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    @Override
    public void createAccount(Long userId, CreateAccountRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ERR_03.getErrorCode(), ERR_03.getErrorDescription()));
        AccountEntity accountEntity = AccountEntity.builder()
                .bank(request.bank())
                .password(request.password())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .currency(request.currency())
                .user(userEntity)
                .build();

        accountRepository.save(accountEntity);
    }

    @Override
    @Transactional
    public void deleteAccount(Long userId, Long accountId) {
        AccountEntity accountEntity = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new NotFoundException(ERR_01.getErrorCode(), ERR_01.getErrorDescription()));
        accountRepository.delete(accountEntity);
    }

    @Override
    public List<RetrieveAccountResponse> getAccounts(Long userId) {
        return accountRepository.findByUserId(userId).stream().map(accountMapper::mapToDto).toList();
    }

    @Override
    public RetrieveAccountResponse getAccount(Long userId, Long accountId) {
        AccountEntity accountEntity = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new NotFoundException(ERR_01.getErrorCode(), ERR_01.getErrorDescription()));
        return accountMapper.mapToDto(accountEntity);
    }

    @Override
    public AccountEntity getActiveAccount(Long userId, Long accountId) {
        return accountRepository.findByUserIdAndIdAndStatus(userId, accountId, AccountStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException(ERR_04.getErrorCode(), ERR_04.getErrorDescription()));
    }

    @Override
    public AccountEntity getAccountEntityByIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new NotFoundException(ERR_01.getErrorCode(), ERR_01.getErrorCode()));
    }
}
