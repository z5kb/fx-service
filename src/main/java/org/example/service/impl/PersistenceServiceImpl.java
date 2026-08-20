package org.example.service.impl;

import org.example.model.Conversion;
import org.example.persistence.BalanceRepository;
import org.example.persistence.ClientRepository;
import org.example.persistence.ConvertTransactionRepository;
import org.example.persistence.CurrencyRepository;
import org.example.persistence.model.Balance;
import org.example.persistence.model.ConvertTransaction;
import org.example.service.PersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public void convert(Conversion conversion, List<Balance> clientBalances) {
        ConvertTransaction transaction = new ConvertTransaction();
        transaction.setTimestamp(conversion.getTimestamp());
        transaction.setSourceAmount(conversion.getSourceAmount());
        transaction.setSourceCurrency(currencyRepository.findByCode(conversion.getSourceCurrencyCode()).get()); // TODO handle optional
        transaction.setTargetAmount(conversion.getTargetAmount());
        transaction.setTargetCurrency(currencyRepository.findByCode(conversion.getTargetCurrencyCode()).get()); // TODO handle optional
        transaction.setConversionRate(conversion.getConversionRate());
        transaction.setNewSourceBalance(conversion.getNewSourceBalance());
        transaction.setNewTargetBalance(conversion.getNewTargetBalance());
        transaction.setClient(clientRepository.findById(conversion.getClientId()).get()); // TODO handle optional
        transactionRepository.save(transaction);

        // update the balances
        boolean updatedSourceAmount = false;
        boolean updatedTargetAmount = false;
        for (Balance balance : clientBalances) {
            if (balance.getCurrency().getCode().equals(conversion.getSourceCurrencyCode())) {
                balance.setAmount(conversion.getNewSourceBalance());
                updatedSourceAmount = true;
                balanceRepository.save(balance);
            } else if (balance.getCurrency().getCode().equals(conversion.getTargetCurrencyCode())) {
                balance.setAmount(conversion.getNewTargetBalance());
                updatedTargetAmount = true;
                balanceRepository.save(balance);
            }
        }

        if (!updatedSourceAmount || !updatedTargetAmount) { // TODO better exception
            throw new IllegalStateException("Failed to update account balances. Reverting everything.");
        }
    }
}
