package com.beneboba.order_service.service;

import com.beneboba.order_service.model.Order;
import com.beneboba.order_service.model.OrderItem;
import com.beneboba.order_service.model.OrderRequest;
import com.beneboba.order_service.model.OrderStatus;
import com.beneboba.order_service.model.event.SagaEvent;
import com.beneboba.order_service.model.event.SagaEventType;
import com.beneboba.order_service.repository.OrderRepository;
import com.beneboba.order_service.repository.OrderItemRepository;
import com.beneboba.order_service.util.ObjectConverter;
import com.beneboba.order_service.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ObjectConverter objectConverter;

    public Mono<OrderRequest> createOrder(OrderRequest request) {
        request.getOrder().setOrderStatus(OrderStatus.CREATED);
        request.getOrder().setOrderDate(LocalDate.now());

        return validationService.validate(request)
                .flatMap(valid -> orderRepository.save(request.getOrder()))
                .flatMap(order -> {
                    List<OrderItem> orderItems = request.getProducts();
                    orderItems.forEach(orderItem -> orderItem.setOrderId(order.getId()));

                    request.setProducts(orderItems);

                    return orderItemRepository.saveAll(orderItems)
                            .collectList()
                            .thenReturn(request);
                })
                .doOnSuccess(savedOrder -> {
                    log.info("savedOrder :: {}", savedOrder);
                    SagaEvent event = new SagaEvent(savedOrder.getOrder().getId(), SagaEventType.ORDER_CREATED, savedOrder);
                    log.info("event obj :: {}", event);
                    String strEvent = objectConverter.convertObjectToString(event);
                    log.info("event str :: {}", strEvent);
                    kafkaTemplate.send("saga-topic", strEvent);
                });

//        return orderRepository.save(order.getOrder())
//                .flatMap(savedOrder -> {
//                    order.getOrderItems().forEach(item -> item.setOrderId(savedOrder.getId()));
//                    return orderRepository.save(savedOrder);
//                })
//
//
//
//                .doOnSuccess(savedOrder -> {
//                    SagaEvent event = new SagaEvent(savedOrder.getId().toString(), SagaEventType.ORDER_CREATED, savedOrder);
//                    kafkaTemplate.send("saga-topic", event);
//                });
    }

    public Mono<Order> getOrder(Long orderId, Long customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId);
    }

    public Mono<Order> updateOrderStatus(Long orderId, OrderStatus status) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setOrderStatus(status);
                    return orderRepository.save(order);
                });
    }

    public Mono<Void> cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    return orderRepository.save(order);
                })
                .then();
    }

    public Flux<Order> getAllOrders(){
        return orderRepository.findAll();
    }
}