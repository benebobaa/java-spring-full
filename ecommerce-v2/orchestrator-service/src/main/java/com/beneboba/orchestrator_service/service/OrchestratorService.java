package com.beneboba.orchestrator_service.service;

import com.beneboba.orchestrator_service.model.*;
import com.beneboba.orchestrator_service.util.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OrchestratorService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectConverter objectConverter;

    @KafkaListener(topics = "saga-topic", groupId = "bene-group")
    public void handleSagaEvents(String sagaEvent) {
        log.info("sagaEvent before convert :: {}", sagaEvent);
        SagaEvent event = objectConverter.convertStringToObject(sagaEvent, SagaEvent.class);
        log.info("sagaEvent after convert :: {}", event);

//        Long sagaId = event.getSagaId();

        log.info("handleSagaEvents :: {}", event);
        switch (event.getType()) {
            case ORDER_CREATED:
                handleOrderCreated(event);
                break;
            case PRODUCT_RESERVED:
                handleProductReserved(event);
                break;
            case PRODUCT_RESERVATION_FAILED:
                handleProductReservationFailed(event);
                break;
            case PAYMENT_PROCESSED:
                handlePaymentProcessed(event);
                break;
            case PAYMENT_FAILED:
                handlePaymentFailed(event);
                break;
            case PRODUCT_RELEASED:
                break;
            default:
                throw new IllegalStateException("Unexpected event type: " + event.getType());
        }
    }

    private void handleOrderCreated(SagaEvent event) {
        log.info("handleOrderCreated :: {}", event);
//        OrderEvent order = event.getOrderRequest();

        // Request product reservation
//        SagaEvent reserveProductEvent = new SagaEvent(event.getSagaId(), SagaEventType.ORDER_CREATED, order);
        String eventStr = objectConverter.convertObjectToString(event);
        log.info("Sending message to PRODUCT-TOPIC :: {}", eventStr);
        kafkaTemplate.send("product-topic", eventStr);
    }

    private void handleProductReserved(SagaEvent event) {
        log.info("handleProductReserved :: {}", event);

        String eventStr = objectConverter.convertObjectToString(event);
        log.info("Sending event to PAYMENT-TOPIC :: {}", eventStr);
        kafkaTemplate.send("payment-topic", eventStr);
    }

    private void handleProductReservationFailed(SagaEvent event) {
        log.info("handleProductReservationFailed event:: {}", event);

        cancelOrder(event.getOrderRequest());
    }

    private void handlePaymentProcessed(SagaEvent event) {
        log.info("handlePaymentProcessed :: {}", event);

        SagaEvent completeSagaOrder = new SagaEvent(event.getSagaId(),
                SagaEventType.SAGA_COMPLETED, event.getOrderRequest());

        String eventStr = objectConverter.convertObjectToString(completeSagaOrder);
        log.info("Sending complete saga order event :: {}", eventStr);
        kafkaTemplate.send("order-topic", eventStr);
    }

    private void handlePaymentFailed(SagaEvent event) {
        log.info("handlePaymentFailed :: {}", event);

        cancelOrder(event.getOrderRequest());
    }

//    private void completeSaga(SagaEvent event) {
//        event.setType(event.getType() == SagaEventType.SAGA_COMPLETED ? SagaStatus.COMPLETED : SagaStatus.FAILED);
//        SagaEvent completionEvent = new SagaEvent(state.getSagaId(), eventType, state.getOrderEvent());
//
//        String eventStr = objectConverter.convertObjectToString(completionEvent);
//        kafkaTemplate.send("saga-completion-topic", eventStr);
//    }

//    private PaymentDetails createPaymentDetails(Order order) {
//        // Logic to create payment details from the order
//        return new PaymentDetails(order.getId(), order.getTotalAmount());
//    }
//
    private void cancelOrder(OrderEvent order) {
        log.info("cancel order");

        SagaEvent cancelOrderEvent = new SagaEvent(order.getOrder().getId(),
                SagaEventType.SAGA_FAILED, order);

        String eventStr = objectConverter.convertObjectToString(cancelOrderEvent);
        log.info("Sending cancel order :: {}", eventStr);
        kafkaTemplate.send("order-topic", eventStr);
    }

    private void releaseProductReservations(SagaEvent sagaEvent) {
        log.info("releaseProductReservations :: {}", sagaEvent);

        SagaEvent releaseReservationsEvent = new SagaEvent(null, SagaEventType.SAGA_FAILED, reservations);
        kafkaTemplate.send("product-topic", releaseReservationsEvent);
    }
}