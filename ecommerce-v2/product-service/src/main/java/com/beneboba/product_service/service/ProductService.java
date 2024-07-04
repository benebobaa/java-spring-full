package com.beneboba.product_service.service;

import com.beneboba.product_service.model.Product;
import com.beneboba.product_service.model.event.*;
import com.beneboba.product_service.repository.ProductRepository;
import com.beneboba.product_service.util.ObjectConverter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectConverter objectConverter;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> createProduct(Product product) {
        return productRepository.save(product)
                .doOnSuccess(savedProduct -> {
                    SagaEvent event = new SagaEvent(savedProduct.getId(), SagaEventType.PRODUCT_CREATED, null);
                    kafkaTemplate.send("saga-topic",
                            objectConverter.convertObjectToString(event));
                });
    }

    public Mono<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setImageUrl(product.getImageUrl());
                    existingProduct.setStockQuantity(product.getStockQuantity());
                    existingProduct.setUpdatedAt(LocalDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .doOnSuccess(updatedProduct -> {
                    SagaEvent event = new SagaEvent(updatedProduct.getId(), SagaEventType.PRODUCT_UPDATED, null);
                    kafkaTemplate.send("saga-topic",
                            objectConverter.convertObjectToString(event));
                });
    }

//    public Mono<Void> deleteProduct(Long id) {
//        return productRepository.deleteById(id)
//                .doOnSuccess(v -> {
//                    SagaEvent event = new SagaEvent(id.toString(), SagaEventType.PRODUCT_DELETED, id);
//                    kafkaTemplate.send("saga-topic", event);
//                });
//    }

    public Mono<Void> reserveProducts(OrderEvent orderEvent) {

        AtomicReference<Float> totalAmount = new AtomicReference<>(0.0f);

        return Flux.fromIterable(orderEvent.getProducts())
                .flatMap(productEvent -> {
                    Long productId = productEvent.getProductId();
                    int quantity = productEvent.getQuantity();

                    return productRepository.findById(productId)
                            .flatMap(product -> {
                                if (product.getStockQuantity() >= quantity) {
                                    product.setStockQuantity(product.getStockQuantity() - quantity);
                                    return productRepository.save(product)
                                            .doOnSuccess(savedProduct -> {
                                                log.info("Product reserved :: id {}", productId);
                                                productEvent.setPrice(product.getPrice());

                                                // Accumulate the total amount
                                                float productTotal = product.getPrice() * quantity;
                                                totalAmount.updateAndGet(amount -> amount + productTotal);
                                            });
                                } else {
                                    log.error("Not enough stock for product :: id {}", productId);
                                    return Mono.error(new Exception("Not enough stock for product: " + productId));
                                }
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                log.error("Product not found :: id {}", productId);
                                SagaEvent event = new SagaEvent(productId,
                                        SagaEventType.PRODUCT_RESERVATION_FAILED, orderEvent);
                                kafkaTemplate.send("saga-topic",
                                        objectConverter.convertObjectToString(event));
                                return Mono.error(new Exception("Product not found: " + productId));
                            }))
                            .doOnError(error -> {
                                log.error("Product reservation failed :: id {}", productId);
                                SagaEvent event = new SagaEvent(productId,
                                        SagaEventType.PRODUCT_RESERVATION_FAILED, orderEvent);
                                kafkaTemplate.send("saga-topic",
                                        objectConverter.convertObjectToString(event));
                            });
                })
                .then()
                .doOnSuccess(v -> {
                    orderEvent.getOrder().setTotalAmount(totalAmount.get());
                    SagaEvent event = new SagaEvent(orderEvent.getOrder().getId(),
                            SagaEventType.PRODUCT_RESERVED, orderEvent);

                    log.info("Sending message PRODUCT_RESERVED :: {}", event);
                    kafkaTemplate.send("saga-topic",
                            objectConverter.convertObjectToString(event));
                });
    }

    public Mono<Product> releaseProduct(Long productId, int quantity) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    product.setStockQuantity(product.getStockQuantity() + quantity);
                    return productRepository.save(product);
                })
                .doOnSuccess(releasedProduct -> {
                    SagaEvent event = new SagaEvent(releasedProduct.getId(), SagaEventType.PRODUCT_RELEASED, null);
                    kafkaTemplate.send("saga-topic",
                            objectConverter.convertObjectToString(event));
                });
    }
}