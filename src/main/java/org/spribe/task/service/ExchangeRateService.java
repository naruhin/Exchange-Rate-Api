package org.spribe.task.service;

import org.spribe.task.client.ExchangeRateApiClient;
import org.spribe.task.domain.Currency;
import org.spribe.task.domain.ExchangeRate;
import org.spribe.task.repository.CurrencyRepository;
import org.spribe.task.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExchangeRateService extends AbstractCrudService<ExchangeRate, UUID> {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateApiClient apiClient;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               CurrencyRepository currencyRepository,
                               ExchangeRateApiClient apiClient) {
        super(exchangeRateRepository);
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
        this.apiClient = apiClient;
    }

    public void updateExchangeRates() {
        List<Currency> currencies = currencyRepository.findAll();
        if (!currencies.isEmpty()) {
            currencies.forEach(currency -> {
                Map<String, Double> rates = apiClient.fetchExchangeRates(currency.getCode());
                if (rates != null) {
                    processExchangeRates(currency, rates);
                }
            });
        }
    }

    private void processExchangeRates(Currency baseCurrency, Map<String, Double> rates) {
        rates.forEach((targetCurrency, rate) -> updateExchangeRate(baseCurrency, targetCurrency, rate));
    }

    public void updateExchangeRate(Currency baseCurrency, String targetCurrency, double rate) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                .orElseGet(() -> {
                    ExchangeRate newRate = new ExchangeRate();
                    newRate.setCurrency(baseCurrency);
                    newRate.setTargetCurrency(targetCurrency.length() > 3 ? targetCurrency.substring(3) : targetCurrency);
                    return newRate;
                });

        exchangeRate.setRate(rate);
        exchangeRate.setTimestamp(System.currentTimeMillis());

        exchangeRateRepository.save(exchangeRate);
    }


    public List<ExchangeRate> getExchangeRatesForCurrency(String currencyCode) {
        Optional<Currency> currency = currencyRepository.findByCode(currencyCode.toUpperCase());
        if (currency.isPresent()) {
            return currency.get().getExchangeRates();
        } else {
            throw new IllegalArgumentException("Currency with code " + currencyCode + " not found.");
        }
    }

}