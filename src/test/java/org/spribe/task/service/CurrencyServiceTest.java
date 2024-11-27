package org.spribe.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.spribe.task.domain.Currency;
import org.spribe.task.repository.CurrencyRepository;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldCreateNewCurrency_WhenCodeDoesNotExist() {
        String currencyCode = "USD";
        when(currencyRepository.existsByCodeIgnoreCase(currencyCode)).thenReturn(false);

        Currency savedCurrency = new Currency();
        savedCurrency.setId(UUID.randomUUID());
        savedCurrency.setCode(currencyCode);
        when(currencyRepository.save(any(Currency.class))).thenReturn(savedCurrency);

        Currency result = currencyService.create(currencyCode);

        assertNotNull(result);
        assertEquals(currencyCode, result.getCode());
        verify(currencyRepository, times(1)).existsByCodeIgnoreCase(currencyCode);
        verify(currencyRepository, times(1)).save(any(Currency.class));
    }

    @Test
    void create_ShouldThrowException_WhenCodeAlreadyExists() {
        String currencyCode = "USD";
        when(currencyRepository.existsByCodeIgnoreCase(currencyCode)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                currencyService.create(currencyCode)
        );

        assertEquals("Currency with code USD already exists.", exception.getMessage());
        verify(currencyRepository, times(1)).existsByCodeIgnoreCase(currencyCode);
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void create_ShouldThrowException_WhenSaveFails() {
        String currencyCode = "USD";
        when(currencyRepository.existsByCodeIgnoreCase(currencyCode)).thenReturn(false);
        when(currencyRepository.save(any(Currency.class))).thenThrow(new DataIntegrityViolationException("Save failed"));

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () ->
                currencyService.create(currencyCode)
        );

        assertEquals("Save failed", exception.getMessage());
        verify(currencyRepository, times(1)).existsByCodeIgnoreCase(currencyCode);
        verify(currencyRepository, times(1)).save(any(Currency.class));
    }
}
