package com.beneboba.spring_rest_api.controller;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.Amount;
import com.beneboba.spring_rest_api.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AccountControllerTest3 {

    @Mock
    private AccountService accountService;

    @InjectMocks
    AccountController accountController;

    @BeforeEach
    void setMockOutput() {
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(new Account("1982094121",10000));
        accountList.add(new Account("1982094122",10000));
        accountList.add(new Account("1982094123",10000));
        accountList.add(new Account("1982094124",10000));
        accountList.add(new Account("1982094125",10000));

        lenient().when(accountService.listAll()).thenReturn(accountList);
        lenient().when(accountService.save(any())).thenReturn(new Account("1982094125",10000) );
        lenient().when(accountService.get(anyInt())).thenReturn(new Account("11111",1000));
        lenient().when(accountService.withdraw(anyFloat(), anyInt())).thenReturn(new Account("11111",1000));
    }

    @Test
    public void whenGetRequestToAccounts_thenCorrectResponse() throws Exception {
        log.info("assert GET /api/accounts is correct response");
        List<Account> accountList = accountController.listAll();

        assertEquals(5, accountList.size());
        verify(accountService, times(1)).listAll();
    }

    @Test
    void testCreateAccount() {
        Account account = new Account("1982094125",10000);

        assertEquals(accountController.add(account).getBody().getAccountNumber(), "1982094125");

        verify(accountService, times(1)).save(account);
    }

    @Test
    void testWithdraw() {
        Amount amount = new Amount();
        amount.setAmount(1000);
        accountController.withdraw(1, amount);
        verify(accountService, times(1)).withdraw(amount.getAmount(), 1);
    }
}