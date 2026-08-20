package org.example.service;


import org.example.model.Balance;
import org.example.model.Conversion;
import org.example.model.CreateCurrencyConversionRequest;
import org.example.persistence.model.ConvertTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FxService {

    void prepareFxRatesData(String currency);

    // get the current exchange rate between 2 currencies
    public BigDecimal getExchangeRate(String from, String to);

    // get the balances of a client
    public List<Balance> getBalances(Long clientId);

    // perform a conversion between 2 currencies
    public Conversion createConversion(CreateCurrencyConversionRequest createCurrencyConversionRequest);

    // get currency conversions
    public Page<Conversion> getConversions(Long transactionId, LocalDateTime timestamp, Long clientId, Pageable pageable);
}
