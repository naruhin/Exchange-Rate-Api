package org.spribe.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spribe.task.domain.Currency;
import org.spribe.task.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void testGetAllCurrencies() throws Exception {
        mockMvc.perform(post("/api/currencies")
                        .param("currencyCode", "USD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/currencies")
                        .param("currencyCode", "EUR")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is("USD")))
                .andExpect(jsonPath("$[1].code", is("EUR")));

        List<Currency> currencies = currencyRepository.findAll();
        assertEquals(2, currencies.size());

        Currency usd = currencies.stream()
                .filter(c -> "USD".equals(c.getCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("USD not found in database"));

        Currency eur = currencies.stream()
                .filter(c -> "EUR".equals(c.getCode()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("EUR not found in database"));

        assertEquals("USD", usd.getCode());
        assertEquals("EUR", eur.getCode());
    }

    @Test
    void testCreateCurrency() throws Exception {
        mockMvc.perform(post("/api/currencies")
                        .param("currencyCode", "GBP")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("GBP")));

        List<Currency> currencies = currencyRepository.findAll();
        assertEquals(1, currencies.size());

        Currency gbp = currencies.getFirst();
        assertEquals("GBP", gbp.getCode());
    }
}
