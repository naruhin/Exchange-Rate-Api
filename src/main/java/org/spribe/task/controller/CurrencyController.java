package org.spribe.task.controller;

import org.spribe.task.controller.api.CurrencyApi;
import org.spribe.task.domain.Currency;
import org.spribe.task.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CurrencyController implements CurrencyApi {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @Override
    public ResponseEntity<Currency> create(@RequestParam String currencyCode) {
        Currency newCurrency = currencyService.create(currencyCode);
        return ResponseEntity.ok(newCurrency);
    }
}