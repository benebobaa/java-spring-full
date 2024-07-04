package com.beneboba.orchestrator_service.model;

public enum SagaStatus {
    STARTED,
    PRODUCT_RESERVED,
    PAYMENT_PROCESSED,
    COMPLETED,
    FAILED
}