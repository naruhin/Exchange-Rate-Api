package org.spribe.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spribe.task.domain.Currency;
import org.spribe.task.domain.ExchangeRate;
import org.spribe.task.repository.CurrencyRepository;
import org.spribe.task.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();

        Currency usd = currencyRepository.save(new Currency("USD"));
        exchangeRateRepository.saveAll(Arrays.asList(
                new ExchangeRate(usd, "GBP", 0.75),
                new ExchangeRate(usd, "EUR", 0.85),
                new ExchangeRate(usd, "JPY", 110.00)
        ));
    }

    @Test
    void testGetExchangeRates() throws Exception {
        String currencyCode = "USD";

        mockMvc.perform(get("/api/exchange-rates")
                        .param("currencyCode", currencyCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].targetCurrency", is("GBP")))
                .andExpect(jsonPath("$[0].rate", is(0.75)))
                .andExpect(jsonPath("$[1].targetCurrency", is("EUR")))
                .andExpect(jsonPath("$[1].rate", is(0.85)))
                .andExpect(jsonPath("$[2].targetCurrency", is("JPY")))
                .andExpect(jsonPath("$[2].rate", is(110.00)));

        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();
        assertEquals(3, exchangeRates.size());

        ExchangeRate firstRate = exchangeRates.getFirst();
        assertEquals("USD", firstRate.getCurrency().getCode());
        assertEquals("GBP", firstRate.getTargetCurrency());
        assertEquals(0.75, firstRate.getRate());
    }

}