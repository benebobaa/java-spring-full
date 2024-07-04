package com.beneboba.spring_rest_api.controller;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.Amount;
import com.beneboba.spring_rest_api.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> listAll() {
        return accountService.listAll();
    }

    @GetMapping("/{id}")
    public HttpEntity<Account> getOne(@PathVariable("id") Integer id) {
        try {
            Account account = accountService.get(id);

            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public HttpEntity<Account> add(@Valid @RequestBody Account account) {
        Account savedAccount = accountService.save(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public HttpEntity<Account> replace(@PathVariable("id") Integer id, @RequestBody Account account) {
        account.setId(id);
        Account updatedAccount = accountService.save(account);

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deposits")
    public HttpEntity<Account> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.deposit(amount.getAmount(), id);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping("/{id}/withdrawal")
    public HttpEntity<Account> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.withdraw(amount.getAmount(), id);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}