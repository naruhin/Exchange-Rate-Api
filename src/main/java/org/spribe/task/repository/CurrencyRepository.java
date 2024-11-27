package org.spribe.task.repository;

import org.spribe.task.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
    Optional<Currency> findByCode(String code);
    boolean existsByCodeIgnoreCase(String code);
}