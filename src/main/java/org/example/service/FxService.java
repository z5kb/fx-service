package org.example.service;


import org.example.model.Balance;
import org.example.persistence.model.ConvertTransaction;

import java.math.BigDecimal;
import java.util.List;

public interface FxService {

    void prepareFxRatesData(String currency);

    // get the current exchange rate between 2 currencies
    public BigDecimal getConversionRate(String from, String to);

    public List<Balance> getBalances(Long clientId);

    public ConvertTransaction createConversion(Long clientId, String sourceCurrency, BigDecimal sourceAmount, String targetCurrency);
}
