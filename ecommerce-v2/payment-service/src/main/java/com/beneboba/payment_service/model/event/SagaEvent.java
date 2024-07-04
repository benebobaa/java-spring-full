package com.beneboba.payment_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class SagaEvent {

    private Long sagaId;

    private SagaEventType type;

    private OrderEvent orderRequest;
}
