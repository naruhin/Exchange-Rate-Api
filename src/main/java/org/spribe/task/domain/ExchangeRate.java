package org.spribe.task.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue
    private UUID id; // Use UUID for the primary key

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private String targetCurrency; // Target currency (e.g., "EUR")

    @Column(nullable = false)
    private Double rate; // Exchange rate value

    @Column(nullable = false)
    private Long timestamp; // Timestamp of rate fetching

    public ExchangeRate(Currency currency, String targetCurrency, Double rate) {
        this.currency = currency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.timestamp = System.currentTimeMillis();
    }

    public ExchangeRate() {
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}