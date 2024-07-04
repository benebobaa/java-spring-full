package com.beneboba.order_service.controller;

import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.model.OrderRequest;
import com.beneboba.order_service.model.OrderStatus;
import com.beneboba.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Flux<Order> getAll() {
        return orderService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderRequest> createOrder(@RequestBody OrderRequest order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{orderId}")
    public Mono<Order> getOrder(@PathVariable Long orderId, @RequestParam Long customerId) {
        return orderService.getOrder(orderId, customerId);
    }

    @PutMapping("/{orderId}/status")
    public Mono<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}