package com.beneboba.orchestrator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OrderEvent {

    private Order order;

    private List<ProductEvent> products;
}
