package org.spribe.task.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.spribe.task.domain.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Currency API", description = "API for managing currencies")
@RequestMapping("/api/currencies")
public interface CurrencyApi {

    @GetMapping
    @Operation(summary = "Get all currencies", description = "Retrieve a list of all available currencies")
    ResponseEntity<List<Currency>> getAll();

    @PostMapping
    @Operation(summary = "Create a new currency", description = "Add a new currency to the system")
    ResponseEntity<Currency> create(
            @Parameter(
                    description = "The code of the currency to add",
                    example = "USD"
            )
            @RequestParam String currencyCode
    );
}
