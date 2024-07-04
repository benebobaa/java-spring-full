package com.beneboba.order_service.model;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @Valid
    private Order order;

    @Valid
    private List<OrderItem> products;
}
