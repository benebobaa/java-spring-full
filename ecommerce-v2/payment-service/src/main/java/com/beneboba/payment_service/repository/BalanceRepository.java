package com.beneboba.payment_service.repository;

import com.beneboba.payment_service.model.Balance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BalanceRepository extends ReactiveCrudRepository<Balance, Long> {
    Mono<Balance> findByCustomerId(Long customerId);
}