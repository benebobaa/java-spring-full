package com.beneboba.orchestrator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SagaEvent {

    private Long sagaId;

    private SagaEventType type;

    private OrderEvent orderRequest;
}
