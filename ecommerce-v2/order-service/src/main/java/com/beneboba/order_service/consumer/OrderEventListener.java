package com.beneboba.order_service.consumer;

import com.beneboba.order_service.model.OrderStatus;
import com.beneboba.order_service.model.event.SagaEvent;
import com.beneboba.order_service.service.OrderService;
import com.beneboba.order_service.util.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectConverter objectConverter;

    @KafkaListener(topics = "order-topic", groupId = "bene-group")
    public void handleOrderEvents(String sagaEvent) {
        log.info("handleOrderEvents :: {}", sagaEvent);
        SagaEvent event = objectConverter.convertStringToObject(sagaEvent, SagaEvent.class);

        switch (event.getType()) {
            case SAGA_COMPLETED:
                orderService.updateOrderStatus(event.getSagaId(), OrderStatus.COMPLETED).subscribe();
                break;
            case SAGA_FAILED:
                orderService.cancelOrder(event.getSagaId()).subscribe();
                break;
            // Handle other event types if needed
        }
    }
}