package az.edu.turing.bankingservice.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByCurrencyCode(String currencyCode);

    Optional<ExchangeRate> findFirstByOrderByLastUpdatedDesc();
}
