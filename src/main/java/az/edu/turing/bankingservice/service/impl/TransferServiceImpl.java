package az.edu.turing.bankingservice.service.impl;

import az.edu.turing.bankingservice.dao.entity.AccountEntity;
import az.edu.turing.bankingservice.dao.entity.TransactionEntity;
import az.edu.turing.bankingservice.dao.repository.AccountRepository;
import az.edu.turing.bankingservice.dao.repository.BankTransferRepository;
import az.edu.turing.bankingservice.dao.repository.UserRepository;
import az.edu.turing.bankingservice.exceptions.BadRequestException;
import az.edu.turing.bankingservice.model.dto.request.BankTransferRequest;
import az.edu.turing.bankingservice.model.dto.request.TopUpRequest;
import az.edu.turing.bankingservice.model.enums.Currency;
import az.edu.turing.bankingservice.model.enums.Direction;
import az.edu.turing.bankingservice.service.AccountService;
import az.edu.turing.bankingservice.service.EmailService;
import az.edu.turing.bankingservice.service.TransferService;
import az.edu.turing.bankingservice.utils.CurrencyRateFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static az.edu.turing.bankingservice.model.enums.AccountStatus.ACTIVE;
import static az.edu.turing.bankingservice.model.enums.Error.*;
import static az.edu.turing.bankingservice.utils.TransactionMessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BankTransferRepository bankTransferRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public void makeBankTransfer(Long userId, Long accountId, BankTransferRequest transferDto) {
        AccountEntity fromAccount = accountService.getActiveAccount(userId, accountId);
        if (fromAccount.getIban().equals(transferDto.toIban())) {
            throw new BadRequestException(ERR_05.getErrorCode(), ERR_05.getErrorDescription());
        }

        AccountEntity toAccount = accountService.getAccountEntityByIban(transferDto.toIban());
        log.info("status {}", toAccount.getStatus());
        if (!ACTIVE.equals(toAccount.getStatus())) {
            throw new BadRequestException(ERR_04.getErrorCode(), ERR_04.getErrorDescription());
        }

        BigDecimal commission = calculateCommission(fromAccount, toAccount, transferDto.amount());
        BigDecimal totalAmount = transferDto.amount().add(commission);


        if (bankTransferIsPossible(fromAccount.getBalance(), totalAmount)) {
            BigDecimal addedAmount = convert(transferDto.amount(), fromAccount.getCurrency(), toAccount.getCurrency());

            fromAccount.setBalance(fromAccount.getBalance().subtract(totalAmount));
            accountRepository.save(fromAccount);

            toAccount.setBalance(toAccount.getBalance().add(addedAmount));
            accountRepository.save(toAccount);

            saveTransaction(fromAccount, Direction.OUTGOING, transferDto.amount(), commission.toString(), getTransactionMessage(fromAccount, toAccount));
            saveTransaction(toAccount, Direction.INCOMING, transferDto.amount(), "UNKNOWN", getTransactionMessage(fromAccount, toAccount));
        }
    }

    private String getTransactionMessage(AccountEntity fromAccount, AccountEntity toAccount) {
        return fromAccount.getUser().getId().equals(toAccount.getUser().getId()) ? BETWEEN_MY_CARDS : ACCOUNT_TO_ANOTHER_ACCOUNT;
    }

    private BigDecimal calculateCommission(AccountEntity fromAccount, AccountEntity toAccount, BigDecimal amount) {
        return fromAccount.getBank().equals(toAccount.getBank()) ? BigDecimal.ZERO : amount.divideToIntegralValue(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
    }

    private void saveTransaction(AccountEntity account, Direction direction, BigDecimal amount, String commission, String message) {
        TransactionEntity transaction = TransactionEntity.builder()
                .direction(direction)
                .comission(commission)
                .message(message)
                .amount(amount)
                .account(account).build();
        bankTransferRepository.save(transaction);
    }

    @Transactional
    @Override
    public void topUp(Long userId, Long accountId, TopUpRequest request) {
        AccountEntity toAccount = accountService.getActiveAccount(userId, accountId);
        if (toAccount.getIban().equals(request.fromIban())) {
            throw new BadRequestException(ERR_05.getErrorCode(), ERR_05.getErrorDescription());
        }
        AccountEntity fromAccount = accountService.getAccountEntityByIban(request.fromIban());

        log.info("status {}", fromAccount.getStatus());

        if (!ACTIVE.equals(fromAccount.getStatus())) {
            throw new BadRequestException(ERR_04.getErrorCode(), ERR_04.getErrorDescription());
        }

        BigDecimal commission = calculateCommission(fromAccount, toAccount, request.amount());
        BigDecimal totalAmount = request.amount().add(commission);


        if (bankTransferIsPossible(fromAccount.getBalance(), totalAmount)) {

            if (!fromAccount.getUser().getId().equals(userId)) {
                throw new BadRequestException(ERR_07.getErrorCode(), ERR_07.getErrorDescription());
            }

            BigDecimal addedAmount = convert(request.amount(), fromAccount.getCurrency(), toAccount.getCurrency());

            fromAccount.setBalance(fromAccount.getBalance().subtract(totalAmount));
            accountRepository.save(fromAccount);

            toAccount.setBalance(toAccount.getBalance().add(addedAmount));
            accountRepository.save(toAccount);

            saveTransaction(fromAccount, Direction.OUTGOING, request.amount(), commission.toString(), TOP_UP);
            saveTransaction(toAccount, Direction.INCOMING, request.amount(), "UNKNOWN", TOP_UP);
        }
    }

    public BigDecimal convert(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
        try {
            CurrencyRateFetcher.fetchExchangeRates();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }

        if (fromCurrency == toCurrency) {
            return amount;
        }

        BigDecimal fromRate = CurrencyRateFetcher.getRate(fromCurrency);
        BigDecimal toRate = CurrencyRateFetcher.getRate(toCurrency);

        if (fromRate == null || toRate == null) {
            throw new BadRequestException(ERR_08.getErrorCode(), ERR_08.getErrorDescription());
        }

        BigDecimal amountInBaseCurrency = amount.multiply(fromRate);

        return amountInBaseCurrency.divide(toRate, 10, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }


    private boolean bankTransferIsPossible(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new BadRequestException(ERR_06.getErrorCode(), ERR_06.getErrorDescription());
        }
        return true;
    }
}
