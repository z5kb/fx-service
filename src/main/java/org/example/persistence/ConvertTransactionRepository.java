package org.example.persistence;

import org.example.persistence.model.ConvertTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConvertTransactionRepository extends JpaRepository<ConvertTransaction, Long> {
    List<ConvertTransaction> findByClientId(Long clientId);
}