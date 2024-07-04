package com.beneboba.spring_postgres.repository;

import com.beneboba.spring_postgres.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByPrice(double price, Pageable pageable);

    @Query(value = "SELECT * FROM Product p WHERE p.name like %?1%", nativeQuery = true)
    List<Product> findByName(String name);


    Page<Product> findAll(Pageable page);
}
