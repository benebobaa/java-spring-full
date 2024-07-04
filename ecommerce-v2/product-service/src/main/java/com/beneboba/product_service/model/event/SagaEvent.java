package com.beneboba.product_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.annotation.Order;

@AllArgsConstructor
@Data
public class SagaEvent {

    private Long sagaId;

    private SagaEventType type;

    private OrderEvent orderRequest;
}
