package org.spribe.task.repository;

import org.spribe.task.domain.Currency;
import org.spribe.task.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, UUID> {
    Optional<ExchangeRate> findByCurrencyAndTargetCurrency(Currency currency, String targetCurrency);
}