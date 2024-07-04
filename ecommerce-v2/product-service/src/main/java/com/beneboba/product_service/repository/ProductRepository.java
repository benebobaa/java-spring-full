package com.beneboba.product_service.repository;

import com.beneboba.product_service.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("UPDATE products SET quantity = quantity - :reserveQuantity " +
            "WHERE id = :id AND quantity >= :reserveQuantity")
    Mono<Boolean> reserveProduct(Long id, Integer reserveQuantity);
}