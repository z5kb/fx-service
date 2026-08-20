package org.example.service.impl;

import org.example.model.Balance;
import org.example.model.Conversion;
import org.example.model.CreateCurrencyConversionRequest;
import org.example.persistence.model.ConvertTransaction;
import org.example.service.ApiClientService;
import org.example.service.FxService;
import org.example.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class FxServiceImpl implements FxService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final ApiClientService apiClientService;

    private final PersistenceService persistenceService;

    @Autowired
    public FxServiceImpl(ApiClientService apiClientService, PersistenceService persistenceService) {
        this.apiClientService = apiClientService;
        this.persistenceService = persistenceService;
    }

    @Override
    public void prepareFxRatesData(String currency) { // TODO make sure this is multi-thread-safe (probably not)
        fetchFxRatesDataAndReschedule(currency);
    }

    @Override
    public BigDecimal getExchangeRate(String from, String to) {
        return apiClientService.fetch(from).getCurrencyRates().get(to);
    }

    @Override
    public List<Balance> getBalances(Long clientId) {
        List<org.example.persistence.model.Balance> balancesRaw = persistenceService.getBalances(clientId);
        ArrayList<Balance> result = new ArrayList<>();
        for ( org.example.persistence.model.Balance balanceRaw : balancesRaw ) {
            Balance balance = new Balance();
            balance.setCurrencyCode(balanceRaw.getCurrency().getCode());
            balance.setAmount(balanceRaw.getAmount());
            result.add(balance);
        }
        return result;
    }

    @Override
    public Conversion createConversion(CreateCurrencyConversionRequest req, String idempotencyKey) {

        // check for idempotency if the key is provided by the client
        if (idempotencyKey != null) {
            if (!persistenceService.uniqueByIdempotencyKey(idempotencyKey)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A request with this Idempotency-Key is already being processed"
                );
            }
        }

        List<org.example.persistence.model.Balance> clientBalances = persistenceService.getBalances(req.getClientId());
        BigDecimal sourceBalance = null;
        BigDecimal targetBalance = null;
        for (org.example.persistence.model.Balance balance : clientBalances) {
            if (balance.getCurrency().getCode().equals(req.getSourceCurrency())) { sourceBalance = balance.getAmount(); }
            if (balance.getCurrency().getCode().equals(req.getTargetCurrency())) { targetBalance = balance.getAmount(); }
        }
        if (sourceBalance == null || targetBalance == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Client trying to convert between currencies they don't own"
            );
        }

        BigDecimal newSourceBalance = sourceBalance.subtract(req.getAmount());
        if (newSourceBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT,
                    "Client doesn't have enough " + req.getSourceCurrency()
            );
        }

        BigDecimal conversionRate = getExchangeRate(req.getSourceCurrency(), req.getTargetCurrency());
        BigDecimal targetAmountCredit = req.getAmount().multiply(conversionRate) ;
        BigDecimal newTargetBalance = targetBalance.add(targetAmountCredit);

        ConvertTransaction convertTransaction = persistenceService.convert(
                req,
                idempotencyKey,
                conversionRate,
                LocalDateTime.now(),
                targetAmountCredit,
                newTargetBalance,
                newSourceBalance,
                clientBalances
        );

        Conversion conversion = new Conversion();
        conversion.setTimestamp(convertTransaction.getTimestamp());
        conversion.setSourceAmount(convertTransaction.getSourceAmount());
        conversion.setSourceCurrencyCode(convertTransaction.getSourceCurrency().getCode());
        conversion.setTargetAmount(convertTransaction.getTargetAmount());
        conversion.setTargetCurrencyCode(convertTransaction.getTargetCurrency().getCode());
        conversion.setConversionRate(convertTransaction.getConversionRate());
        conversion.setNewSourceBalance(convertTransaction.getNewSourceBalance());
        conversion.setNewTargetBalance(convertTransaction.getNewTargetBalance());
        conversion.setClientId(convertTransaction.getClient().getId());
        return conversion;
    }

    @Override
    public Page<Conversion> getConversions(Long transactionId, LocalDateTime timestamp, Long clientId, Pageable pageable) {

        Page<ConvertTransaction> convertTransactions;
        if (clientId != null) {
            convertTransactions = persistenceService.getCurrencyConversionTransactionsPaginatedByClientId(clientId, pageable);
        } else if (transactionId != null) {
            convertTransactions = persistenceService.getCurrencyConversionTransactionsPaginatedById(transactionId, pageable);
        } else if (timestamp != null) {
            convertTransactions = persistenceService.getCurrencyConversionTransactionsPaginatedByTimestamp(timestamp, pageable);
        } else { // this should never be reached as it's being checked in the controller as well
            throw new IllegalArgumentException("At least 1 parameter was expected to not be null.");
        }

        return convertTransactions.map( rawConversion -> {
            Conversion conversion = new Conversion();
            conversion.setTimestamp(rawConversion.getTimestamp());
            conversion.setSourceAmount(rawConversion.getSourceAmount());
            conversion.setSourceCurrencyCode(rawConversion.getSourceCurrency().getCode());
            conversion.setTargetAmount(rawConversion.getTargetAmount());
            conversion.setTargetCurrencyCode(rawConversion.getTargetCurrency().getCode());
            conversion.setConversionRate(rawConversion.getConversionRate());
            conversion.setNewSourceBalance(rawConversion.getNewSourceBalance());
            conversion.setNewTargetBalance(rawConversion.getNewTargetBalance());
            conversion.setClientId(rawConversion.getClient().getId());
            return conversion;
        });
    }

    private void fetchFxRatesDataAndReschedule(String currency) {
        scheduleFxRatesDataFetch(apiClientService.fetch(currency).getTimeNextUpdateTimestamp(), currency);
    }

    private void scheduleFxRatesDataFetch(Long timestamp, String currency) {
        long delay = timestamp - System.currentTimeMillis() / 1000;
        if (delay <= 0) { // if already expired, run now
            fetchFxRatesDataAndReschedule(currency);
        } else {
            scheduler.schedule(() -> fetchFxRatesDataAndReschedule(currency), delay, TimeUnit.SECONDS);
        }
    }
}
