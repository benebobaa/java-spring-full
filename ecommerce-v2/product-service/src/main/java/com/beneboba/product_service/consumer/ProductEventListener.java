package com.beneboba.product_service.consumer;

import com.beneboba.product_service.model.event.OrderEvent;
import com.beneboba.product_service.model.event.SagaEvent;
import com.beneboba.product_service.service.ProductService;
import com.beneboba.product_service.util.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectConverter objectConverter;

    @KafkaListener(topics = "product-topic", groupId = "bene-group")
    public void handleSagaEvents(String sagaEvent) {
        log.info("handleSagaEvents :: {}", sagaEvent);

        SagaEvent event = objectConverter.convertStringToObject(sagaEvent, SagaEvent.class);

        switch (event.getType()) {
            case ORDER_CREATED:
                handleOrderCreated(event);
                break;
            case SAGA_FAILED:
                handleSagaFailed(event);
                break;
            // Add more cases as needed
        }
    }

    private void handleOrderCreated(SagaEvent event) {
        log.info("handleOrderCreated :: event: {}", event);
        OrderEvent payload = event.getOrderRequest();
            productService.reserveProducts(payload)
                    .subscribe();
    }

    private void handleSagaFailed(SagaEvent event) {
        OrderCreatedPayload payload = (OrderCreatedPayload) event.getPayload();
        for (OrderItem item : payload.getOrderItems()) {
            productService.releaseProduct(item.getProductId(), item.getQuantity())
                    .subscribe();
        }
        OrderEvent payload = event.getOrderRequest();
        for (OrderItem item : payload.getProducts()) {
            productService.reserveProduct(item.getProductId(), item.getQuantity(), payload)
                    .subscribe();
        }
    }
}