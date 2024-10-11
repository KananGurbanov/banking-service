package az.edu.turing.bankingservice.dao.repository;

import az.edu.turing.bankingservice.dao.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransferRepository extends JpaRepository<TransactionEntity, Long> {
}
