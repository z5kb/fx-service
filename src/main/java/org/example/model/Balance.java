package org.example.model;

import java.math.BigDecimal;

public class Balance {

    private String currencyCode;
    private BigDecimal amount;

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

}
