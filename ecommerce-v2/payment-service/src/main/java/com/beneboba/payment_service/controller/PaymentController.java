package com.beneboba.payment_service.controller;

import com.beneboba.payment_service.model.Balance;
import com.beneboba.payment_service.model.Transaction;
import com.beneboba.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Transaction> getAllTransaction() {
        return paymentService.getAllTransaction();
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Balance> getAllCustomerBalance() {
        return paymentService.getAllCustomerBalance();
    }


    @PostMapping("/balance/{customerId}/add")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Balance> addFunds(
            @PathVariable Long customerId,
            @RequestParam Float amount) {
        return paymentService.addFundsToCustomerBalance(customerId, amount);
    }
}