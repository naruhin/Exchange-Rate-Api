package org.spribe.task.controller;

import org.spribe.task.controller.api.ExchangeRateApi;
import org.spribe.task.domain.ExchangeRate;
import org.spribe.task.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExchangeRateController implements ExchangeRateApi {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public ResponseEntity<List<ExchangeRate>> getExchangeRates(@RequestParam String currencyCode) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRatesForCurrency(currencyCode));
    }
}