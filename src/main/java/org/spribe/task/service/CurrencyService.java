package org.spribe.task.service;

import jakarta.transaction.Transactional;
import org.spribe.task.domain.Currency;
import org.spribe.task.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CurrencyService extends AbstractCrudService<Currency, UUID> {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        super(currencyRepository);
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public Currency create(String currencyCode) {
        if (currencyRepository.existsByCodeIgnoreCase(currencyCode)) {
            throw new IllegalArgumentException("Currency with code " + currencyCode + " already exists.");
        }

        Currency newCurrency = new Currency();
        newCurrency.setCode(currencyCode.toUpperCase());
        return currencyRepository.save(newCurrency);
    }
}