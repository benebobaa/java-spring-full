package com.beneboba.payment_service.service;

import com.beneboba.payment_service.exception.CustomerNotFoundException;
import com.beneboba.payment_service.exception.InsufficientFundsException;
import com.beneboba.payment_service.model.Balance;
import com.beneboba.payment_service.model.PaymentStatus;
import com.beneboba.payment_service.model.Transaction;
import com.beneboba.payment_service.model.event.Order;
import com.beneboba.payment_service.model.event.SagaEvent;
import com.beneboba.payment_service.model.event.SagaEventType;
import com.beneboba.payment_service.repository.BalanceRepository;
import com.beneboba.payment_service.repository.TransactionRepository;
import com.beneboba.payment_service.util.Helper;
import com.beneboba.payment_service.util.ObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectConverter objectConverter;

    public Mono<Transaction> processPayment(SagaEvent event) {
        return balanceRepository.findByCustomerId(event.getOrderRequest().getOrder().getCustomerId())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found")))
                .flatMap(customerBalance -> {
                    if (customerBalance.getBalance() < event.getOrderRequest().getOrder().getTotalAmount()) {
                        return Mono.error(new InsufficientFundsException("Insufficient balance"));
                    }

                    customerBalance.setBalance(customerBalance.getBalance() - event.getOrderRequest().getOrder().getTotalAmount());
                    return balanceRepository.save(customerBalance);
                })
                .flatMap(savedBalance -> {
                    Transaction transaction = new Transaction();
                    Order order = event.getOrderRequest().getOrder();
                    transaction.setOrderId(order.getId());
                    transaction.setAmount(order.getTotalAmount());
                    transaction.setMode(Helper.classifyPaymentMethod(order.getPaymentMethod()));
                    transaction.setStatus(PaymentStatus.COMPLETED);
                    transaction.setReferenceNumber(Helper.generateReferenceNumber());
                    transaction.setPaymentDate(LocalDateTime.now());

                    return transactionRepository.save(transaction);
                })
                .doOnSuccess(transaction -> {
                    SagaEvent sagaEvent = new SagaEvent(event.getSagaId(),
                            SagaEventType.PAYMENT_PROCESSED, event.getOrderRequest());
                    log.info("Sending PAYMENT_PROCESSED :: {}", sagaEvent);
                    sendPaymentProcessedEvent(sagaEvent);
                })
                .doOnError(error -> {
                    SagaEvent sagaEvent = new SagaEvent(event.getSagaId(),
                            SagaEventType.PAYMENT_FAILED, event.getOrderRequest());
                    log.error("Payment failed: ", error);
                    log.info("Sending PAYMENT_FAILED :: {}", sagaEvent);
                    sendPaymentFailedEvent(sagaEvent);
                });
    }

    private void sendPaymentProcessedEvent(SagaEvent event) {
        log.info("sendPaymentProcessedEvent");

        kafkaTemplate.send("saga-topic",
                objectConverter.convertObjectToString(event));
    }

    private void sendPaymentFailedEvent(SagaEvent event) {
        log.info("sendPaymentFailedEvent");

        kafkaTemplate.send("saga-topic",
                objectConverter.convertObjectToString(event));
    }

    public Flux<Balance> getAllCustomerBalance() {
        return balanceRepository.findAll();
    }

    public Flux<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public Mono<Balance> addFundsToCustomerBalance(Long customerId, Float amount) {
        return balanceRepository.findByCustomerId(customerId)
                .flatMap(balance -> {
                    balance.setBalance(balance.getBalance() + amount);
                    return balanceRepository.save(balance);
                });
    }
}