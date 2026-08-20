package org.example.persistence.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "convert_transactions")
public class ConvertTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private BigDecimal sourceAmount;

    @ManyToOne
    @JoinColumn(name = "source_currency_id")
    private Currency sourceCurrency;

    private BigDecimal targetAmount;

    @ManyToOne
    @JoinColumn(name = "target_currency_id")
    private Currency targetCurrency;

    private BigDecimal conversionRate;

    private BigDecimal newSourceBalance;

    private BigDecimal newTargetBalance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Default constructor required by JPA
    public ConvertTransaction() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    public BigDecimal getNewSourceBalance() {
        return newSourceBalance;
    }

    public void setNewSourceBalance(BigDecimal newSourceBalance) {
        this.newSourceBalance = newSourceBalance;
    }

    public BigDecimal getNewTargetBalance() {
        return newTargetBalance;
    }

    public void setNewTargetBalance(BigDecimal newTargetBalance) {
        this.newTargetBalance = newTargetBalance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}