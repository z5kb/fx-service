package org.example.persistence;

import org.example.persistence.model.ConvertTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConvertTransactionRepository extends JpaRepository<ConvertTransaction, Long> {
    Optional<Page<ConvertTransaction>> findByClientId(Long clientId, Pageable pageable);

    Optional<Page<ConvertTransaction>> findById(Long id, Pageable pageable);

    Optional<Page<ConvertTransaction>> findByTimestamp(LocalDateTime timestamp, Pageable pageable);

    Optional<List<ConvertTransaction>> findByIdempotencyKey(String idempotencyKey);
}