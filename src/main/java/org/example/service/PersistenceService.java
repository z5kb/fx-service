package org.example.service;

import org.example.model.Conversion;
import org.example.model.CreateCurrencyConversionRequest;
import org.example.persistence.model.Balance;
import org.example.persistence.model.ConvertTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PersistenceService {

    // get the balances of a client
    public List<Balance> getBalances(Long clientId);

    // perform a conversion between 2 currencies
    public ConvertTransaction convert(
            CreateCurrencyConversionRequest conversion,
            BigDecimal conversionRate,
            LocalDateTime timestamp,
            BigDecimal targetAmountCredit,
            BigDecimal newTargetBalance,
            BigDecimal newSourceBalance,
            List<Balance> clientBalances
    );

    // get currency conversions by different parameters, paginated
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedByClientId(Long clientId, Pageable pageable);
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedById(Long id, Pageable pageable);
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedByTimestamp(LocalDateTime timestamp, Pageable pageable);

}