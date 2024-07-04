package com.beneboba.order_service.model.event;

import com.beneboba.order_service.model.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SagaEvent {

    private Long sagaId;

    private SagaEventType type;

    private OrderRequest orderRequest;
}
