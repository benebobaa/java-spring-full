package com.beneboba.product_service.controller;

import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable Long id,
                                       @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

//    @DeleteMapping("/{id}")
//    public Mono<Void> deleteProduct(@PathVariable Long id) {
//        return productService.deleteProduct(id);
//    }
}