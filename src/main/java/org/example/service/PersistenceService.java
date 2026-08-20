package org.example.service;

import org.example.persistence.model.Balance;

import java.math.BigDecimal;
import java.util.List;

public interface PersistenceService {

    public List<Balance> getBalances(Long clientId);
}