package org.spribe.task.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.spribe.task.domain.ExchangeRate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Exchange Rate API", description = "API for managing exchange rates")
@RequestMapping("/api/exchange-rates")
public interface ExchangeRateApi {

    @GetMapping
    @Operation(summary = "Get all exchange rates for particular currency", description = "Retrieve a list of all available exchange rates")
    ResponseEntity<List<ExchangeRate>> getExchangeRates(
            @Parameter(
                    description = "The code of the currency exchange rates to retrieve",
                    example = "USD"
            )
            @RequestParam String currencyCode
    );
}
