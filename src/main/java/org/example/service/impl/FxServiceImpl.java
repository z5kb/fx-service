package org.example.service.impl;

import org.example.model.Balance;
import org.example.model.Conversion;
import org.example.persistence.model.ConvertTransaction;
import org.example.service.ApiClientService;
import org.example.service.FxService;
import org.example.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FxServiceImpl implements FxService {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pollFuture;

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
    public BigDecimal getConversionRate(String from, String to) {
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
    public ConvertTransaction createConversion(Long clientId, String sourceCurrency, BigDecimal sourceAmount, String targetCurrency) {
        List<org.example.persistence.model.Balance> clientBalances = persistenceService.getBalances(clientId);
        BigDecimal sourceBalance = null;
        BigDecimal targetBalance = null;
        for (org.example.persistence.model.Balance balance : clientBalances) {
            if (balance.getCurrency().getCode().equals(sourceCurrency)) { sourceBalance = balance.getAmount(); }
            if (balance.getCurrency().getCode().equals(targetCurrency)) { targetBalance = balance.getAmount(); }
        }
        // TODO throw exception - client trying to convert from/to currency they don't own
        assert sourceBalance != null;
        assert targetBalance != null;

        BigDecimal newSourceBalance = sourceBalance.subtract(sourceAmount);
        // TODO throw exception - client doesn't have enough source money
        assert newSourceBalance.compareTo(BigDecimal.ZERO) >= 0;

        BigDecimal conversionRate = getConversionRate(sourceCurrency, targetCurrency);
        BigDecimal targetAmountCredit = sourceAmount.multiply(conversionRate) ;
        BigDecimal newTargetBalance = targetBalance.add(targetAmountCredit);

        Conversion conversion = new Conversion();
        conversion.setClientId(clientId);
        conversion.setSourceCurrencyCode(sourceCurrency);
        conversion.setSourceAmount(sourceAmount);
        conversion.setTargetCurrencyCode(targetCurrency);
        conversion.setConversionRate(conversionRate);
        conversion.setTimestamp(LocalDateTime.now());
        conversion.setTargetAmount(targetAmountCredit);
        conversion.setNewTargetBalance(newTargetBalance);
        conversion.setNewSourceBalance(newSourceBalance);
        persistenceService.convert(conversion, clientBalances);

        return null;

//        transaction.setTargetAmount(conversion.getTargetAmount());
//        transaction.setNewSourceBalance(conversion.getNewSourceBalance());
//        transaction.setNewTargetBalance(conversion.getNewTargetBalance());
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
