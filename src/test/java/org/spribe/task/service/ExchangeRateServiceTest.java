package org.spribe.task.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.spribe.task.client.ExchangeRateApiClient;
import org.spribe.task.domain.Currency;
import org.spribe.task.domain.ExchangeRate;
import org.spribe.task.repository.CurrencyRepository;
import org.spribe.task.repository.ExchangeRateRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateApiClient apiClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateExchangeRates_ShouldUpdateRates_ForAllCurrencies() {
        Currency currency1 = new Currency();
        currency1.setCode("USD");
        Currency currency2 = new Currency();
        currency2.setCode("EUR");

        List<Currency> currencies = Arrays.asList(currency1, currency2);
        when(currencyRepository.findAll()).thenReturn(currencies);

        Map<String, Double> ratesUSD = Map.of("EUR", 0.85, "GBP", 0.75);
        Map<String, Double> ratesEUR = Map.of("USD", 1.18, "GBP", 0.88);
        when(apiClient.fetchExchangeRates("USD")).thenReturn(ratesUSD);
        when(apiClient.fetchExchangeRates("EUR")).thenReturn(ratesEUR);

        exchangeRateService.updateExchangeRates();

        verify(apiClient, times(1)).fetchExchangeRates("USD");
        verify(apiClient, times(1)).fetchExchangeRates("EUR");
        verify(exchangeRateRepository, atLeastOnce()).save(any(ExchangeRate.class));
    }

    @Test
    void updateExchangeRate_ShouldUpdateExistingExchangeRate() {
        // Arrange
        org.spribe.task.domain.Currency baseCurrency = new Currency();
        baseCurrency.setCode("USD");

        String targetCurrency = "EUR";
        double rate = 0.85;

        ExchangeRate existingRate = new ExchangeRate();
        existingRate.setCurrency(baseCurrency);
        existingRate.setTargetCurrency(targetCurrency);
        existingRate.setRate(0.80);

        when(exchangeRateRepository.findByCurrencyAndTargetCurrency(baseCurrency, targetCurrency))
                .thenReturn(Optional.of(existingRate));

        exchangeRateService.updateExchangeRate(baseCurrency, targetCurrency, rate);

        assertEquals(rate, existingRate.getRate());
        verify(exchangeRateRepository, times(1)).save(existingRate);
    }

    @Test
    void updateExchangeRate_ShouldCreateNewExchangeRate_WhenNoneExists() {
        Currency baseCurrency = new Currency();
        baseCurrency.setCode("USD");

        String targetCurrency = "EUR";
        double rate = 0.85;

        when(exchangeRateRepository.findByCurrencyAndTargetCurrency(baseCurrency, targetCurrency))
                .thenReturn(Optional.empty());

        exchangeRateService.updateExchangeRate(baseCurrency, targetCurrency, rate);

        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));
    }

    @Test
    void getExchangeRatesForCurrency_ShouldReturnRates_WhenCurrencyExists() {
        Currency currency = new Currency();
        currency.setCode("USD");
        ExchangeRate rate1 = new ExchangeRate();
        ExchangeRate rate2 = new ExchangeRate();

        currency.setExchangeRates(Arrays.asList(rate1, rate2));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(currency));

        List<ExchangeRate> result = exchangeRateService.getExchangeRatesForCurrency("USD");

        assertEquals(2, result.size());
        assertTrue(result.contains(rate1));
        assertTrue(result.contains(rate2));
    }

    @Test
    void getExchangeRatesForCurrency_ShouldThrowException_WhenCurrencyDoesNotExist() {
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                exchangeRateService.getExchangeRatesForCurrency("USD")
        );

        assertEquals("Currency with code USD not found.", exception.getMessage());
    }
}
