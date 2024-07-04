package com.beneboba.spring_rest_api.controller;
import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.AccountInfo;
import com.beneboba.spring_rest_api.model.AccountWithBalance;
import com.beneboba.spring_rest_api.model.Amount;
import com.beneboba.spring_rest_api.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jdbc/accounts")
public class JdbcAccountController {

    Logger logger = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private AccountService accountService;

    @GetMapping
    public HttpEntity<List<Account>> listAll() {
        return new ResponseEntity<>(accountService.listAllJdbc(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public HttpEntity<Account> getOne(@PathVariable("id") Integer id) {

        Account account = accountService.getJdbc(id);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @PostMapping
    public HttpEntity<Account> add(@RequestBody Account account){

        Account savedAccount = accountService.saveJdbc(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public HttpEntity<Account> replace(@PathVariable("id") Integer id, @RequestBody Account account) {
        account.setId(id);
        Account updatedAccount = accountService.saveJdbc(account);

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deposits")
    public HttpEntity<Account> deposit(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.depositJdbc(amount.getAmount(), id);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping("/{id}/withdrawal")
    public HttpEntity<Account> withdraw(@PathVariable("id") Integer id, @RequestBody Amount amount) {
        Account updatedAccount = accountService.withdrawJdbc(amount.getAmount(), id);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable("id") Integer id) {
        accountService.deleteJdbc(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allwithbalance")
    public HttpEntity<List<AccountWithBalance>> listAllWithBalance(){
        return new ResponseEntity<>(accountService.findAllWithBalance(), HttpStatus.OK);
    }

    @PostMapping("/accountInfo")
    public HttpEntity<AccountInfo> addAccountInfo(@RequestBody AccountInfo accountInfo){
        return new ResponseEntity<>(accountService.saveAccountInfoJdbc(accountInfo), HttpStatus.CREATED);
    }
}
