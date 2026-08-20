package org.example.persistence;

import org.example.persistence.model.ConvertTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConvertTransactionRepository extends JpaRepository<ConvertTransaction, Long> {
    Page<ConvertTransaction> findByClientId(Long clientId, Pageable pageable);

    Page<ConvertTransaction> findById(Long id, Pageable pageable);

    Page<ConvertTransaction> findByTimestamp(LocalDateTime timestamp, Pageable pageable);
}