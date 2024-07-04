package com.beneboba.product_service.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductEvent {
    private Long productId;
    private String eventType;
    private Integer quantity;
}