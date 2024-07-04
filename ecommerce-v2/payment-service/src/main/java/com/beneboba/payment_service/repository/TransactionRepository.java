package com.beneboba.payment_service.repository;

import com.beneboba.payment_service.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    Mono<Transaction> findByOrderId(Long orderId);
}