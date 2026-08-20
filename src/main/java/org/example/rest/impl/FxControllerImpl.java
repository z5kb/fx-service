package org.example.rest.impl;

import org.example.model.Balance;
import org.example.model.Conversion;
import org.example.model.CreateCurrencyConversionRequest;
import org.example.rest.FxController;
import org.example.service.FxService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FxControllerImpl implements FxController {


    private final FxService fxService;

    public FxControllerImpl(FxService fxService) {
        this.fxService = fxService;
    }

    @GetMapping("/rates")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        return ResponseEntity.ok(fxService.getExchangeRate(from, to));
    }

    @GetMapping("/clients/{clientId}/balances")
    public ResponseEntity<List<Balance>> getClientBalances(@PathVariable("clientId") Long clientId) {
        return ResponseEntity.ok(fxService.getBalances(clientId));
    }

    @GetMapping("/conversions")
    public ResponseEntity<Page<Conversion>> getCurrencyConversions(
            @RequestParam(value = "transactionId", required = false) Long transactionId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date,
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        if (transactionId == null && date == null && clientId == null) {
            throw new IllegalArgumentException("At least one filter (transactionId, date, or clientId) is required.");
        }

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fxService.getConversions(transactionId, date, clientId, pageable));
    }

    @PostMapping("/conversions")
    public ResponseEntity<Conversion> performConversion(@RequestBody CreateCurrencyConversionRequest request) {
        Conversion response = fxService.createConversion(request);
        return ResponseEntity.ok(response);
    }
}
