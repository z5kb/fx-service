package org.example.service.impl;

import org.example.model.CreateCurrencyConversionRequest;
import org.example.persistence.BalanceRepository;
import org.example.persistence.ClientRepository;
import org.example.persistence.ConvertTransactionRepository;
import org.example.persistence.CurrencyRepository;
import org.example.persistence.model.Balance;
import org.example.persistence.model.ConvertTransaction;
import org.example.service.PersistenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersistenceServiceImpl implements PersistenceService {

    private final ClientRepository clientRepository;
    private final CurrencyRepository currencyRepository;
    private final BalanceRepository balanceRepository;
    private final ConvertTransactionRepository transactionRepository;

    public PersistenceServiceImpl(
            ClientRepository clientRepository,
            CurrencyRepository currencyRepository,
            BalanceRepository balanceRepository,
            ConvertTransactionRepository transactionRepository
    ) {
        this.clientRepository = clientRepository;
        this.currencyRepository = currencyRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Balance> getBalances(Long clientId) {
        Optional<List<Balance>> result = balanceRepository.findAllByClientId(clientId);
        // TODO throw exception
        assert result.isPresent();
        return result.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConvertTransaction convert(
            CreateCurrencyConversionRequest conversion,
            String idempotencyKey,
            BigDecimal conversionRate,
            LocalDateTime timestamp,
            BigDecimal targetAmountCredit,
            BigDecimal newTargetBalance,
            BigDecimal newSourceBalance,
            List<Balance> clientBalances
    ) {
        ConvertTransaction transaction = new ConvertTransaction();
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setTimestamp(timestamp);
        transaction.setSourceAmount(conversion.getAmount());
        transaction.setSourceCurrency(currencyRepository.findByCode(conversion.getSourceCurrency()).get()); // TODO handle optional
        transaction.setTargetAmount(targetAmountCredit);
        transaction.setTargetCurrency(currencyRepository.findByCode(conversion.getTargetCurrency()).get()); // TODO handle optional
        transaction.setConversionRate(conversionRate);
        transaction.setNewSourceBalance(newSourceBalance);
        transaction.setNewTargetBalance(newTargetBalance);
        transaction.setClient(clientRepository.findById(conversion.getClientId()).get()); // TODO handle optional
        transactionRepository.save(transaction);

        // update the balances
        boolean updatedSourceAmount = false;
        boolean updatedTargetAmount = false;
        for (Balance balance : clientBalances) {
            if (balance.getCurrency().getCode().equals(conversion.getSourceCurrency())) {
                balance.setAmount(newSourceBalance);
                updatedSourceAmount = true;
                balanceRepository.save(balance);
            } else if (balance.getCurrency().getCode().equals(conversion.getTargetCurrency())) {
                balance.setAmount(newTargetBalance);
                updatedTargetAmount = true;
                balanceRepository.save(balance);
            }
        }

        if (!updatedSourceAmount || !updatedTargetAmount) { // TODO better exception
            throw new IllegalStateException("Failed to update account balances. Reverting everything.");
        }

        return transaction;
    }

    @Override
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedByClientId(Long clientId, Pageable pageable) {
        return transactionRepository.findByClientId(clientId, pageable).get(); // TODO handle with a good exception
    }

    @Override
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedById(Long id, Pageable pageable) {
        return transactionRepository.findById(id, pageable).get(); // TODO handle with a good exception
    }

    @Override
    public Page<ConvertTransaction> getCurrencyConversionTransactionsPaginatedByTimestamp(LocalDateTime timestamp, Pageable pageable) {
        return transactionRepository.findByTimestamp(timestamp, pageable).get(); // TODO handle with a good exception
    }

    @Override
    public Boolean uniqueByIdempotencyKey(String idempotencyKey) {
        return transactionRepository.findByIdempotencyKey(idempotencyKey).get().isEmpty(); // TODO handle with a good exception
    }
}
