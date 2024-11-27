package org.spribe.task.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Currency {

    @jakarta.persistence.Id
    @GeneratedValue
    private UUID Id; // Use UUID for the primary key

    @Column(unique = true, nullable = false)
    private String code; // Currency code (e.g., "USD")

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ExchangeRate> exchangeRates;

    public Currency(String code) {
        this.code = code;
    }

    public Currency() {

    }

    // Getters and setters
    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}