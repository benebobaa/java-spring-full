package com.beneboba.spring_rest_api.service;


import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.repository.AccountRepository;
import com.beneboba.spring_rest_api.repository.AccountRepositoryJdbc;
import com.beneboba.spring_rest_api.repository.impl.TestRepository;
import com.beneboba.spring_rest_api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepo;

    @Mock
    TestRepository testRepository;

    @Mock
    AccountRepositoryJdbc accountRepositoryJdbc;

    @InjectMocks
    AccountService accountService = new AccountServiceImpl();


    @BeforeEach
    void setMockOutput() {
//        MockitoAnnotations.openMocks(AccountServiceTest.class);
        List<Account> accountList = new ArrayList<Account>();
        accountList.add(new Account("1982094121",10000));
        accountList.add(new Account("1982094122",10000));
        accountList.add(new Account("1982094123",10000));
        accountList.add(new Account("1982094124",10000));
        accountList.add(new Account("1982094125",10000));

        lenient().when(accountRepo.findAll()).thenReturn(accountList);
        lenient().when(accountRepo.existsById(anyInt())).thenReturn(Boolean.TRUE);
        lenient().when(accountRepo.findById(anyInt())).thenReturn(Optional.of(new Account("11111", 1000)));
    }

    @Test
    void testFindAll() {
        List<Account> accountList = accountService.listAllJdbc2();
        assertEquals(5, accountList.size());
        verify(testRepository, times(1)).findAll();
    }

    @Test
    void testCreateAccount() {
        Account account = new Account("1982094124",10000);
        accountService.saveJdbc(account);
        verify(accountRepositoryJdbc, times(1)).save(account);
    }

    @Test
    void testCreateAccount2() {
        Account account = new Account("1982094127",10000);
        accountService.saveJdbc2(account);
        verify(testRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw() {
        float amount = 1000;
        Integer id = 1;
        accountService.withdraw(amount, id);
        verify(accountRepo, times(1)).withdraw(amount, id);
    }
}
