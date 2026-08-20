package org.example.service.impl;

import org.example.persistence.BalanceRepository;
import org.example.persistence.ClientRepository;
import org.example.persistence.ConvertTransactionRepository;
import org.example.persistence.CurrencyRepository;
import org.example.persistence.model.Balance;
import org.example.service.PersistenceService;
import org.springframework.stereotype.Service;

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
        if (result.isEmpty()) {
            // TODO not found error
        }
        return result.get();
    }
}
