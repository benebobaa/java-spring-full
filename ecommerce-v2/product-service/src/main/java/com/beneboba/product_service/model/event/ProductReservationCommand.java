package com.beneboba.product_service.model.event;

import lombok.Data;

@Data
public class ProductReservationCommand {
    private Long productId;
    private Integer quantity;
}

