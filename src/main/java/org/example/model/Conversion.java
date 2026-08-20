package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Conversion {

    private LocalDateTime timestamp;
    private BigDecimal sourceAmount;
    private String sourceCurrencyCode;
    private BigDecimal targetAmount;
    private String targetCurrencyCode;
    private BigDecimal conversionRate;
    private BigDecimal newSourceBalance;
    private BigDecimal newTargetBalance;
    private Long clientId;

    // Getters and Setters
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

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String sourceCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
