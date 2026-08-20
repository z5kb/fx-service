package org.example.persistence;

import org.example.persistence.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
//    Optional<Balance> findByClientIdAndCurrencyId(Long clientId, Long currencyId);
    Optional<List<Balance>> findAllByClientId(Long clientId);
}