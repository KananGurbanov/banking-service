package az.edu.turing.bankingservice.dao.repository;

import az.edu.turing.bankingservice.dao.entity.AccountEntity;
import az.edu.turing.bankingservice.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByUserId(Long userId);

    Optional<AccountEntity> findByIban(String iban);

    Optional<AccountEntity> findByIdAndUserId(Long id, Long userId);

    Optional<AccountEntity> findByUserIdAndIdAndStatus(Long userId, Long id , AccountStatus status);
}
