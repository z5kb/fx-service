package org.example.rest;

import org.example.model.Balance;
import org.example.model.Conversion;
import org.example.model.CreateCurrencyConversionRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface FxController {

    ResponseEntity<BigDecimal> getExchangeRate(String from, String to);

    ResponseEntity<List<Balance>> getClientBalances(Long clientId);

    ResponseEntity<Page<Conversion>> getCurrencyConversions(
        Long transactionId,
        LocalDateTime date,
        Long clientId,
        int page,
        int size
    );

    ResponseEntity<Conversion> performConversion(@RequestBody CreateCurrencyConversionRequest request);

}
