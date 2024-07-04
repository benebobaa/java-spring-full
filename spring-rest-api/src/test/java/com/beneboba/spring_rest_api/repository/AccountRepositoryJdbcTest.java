package com.beneboba.spring_rest_api.repository;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.repository.impl.AccountRepositoryJdbcImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class AccountRepositoryJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AccountRepositoryJdbcImpl accountRepositoryJdbc;

    private List<Account> accountsDummy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountsDummy = new ArrayList<>();
        accountsDummy.add(new Account("123", 200));
        accountsDummy.add(new Account("124", 201));
    }

    @Test
    void testFindAll() {
        Account account1 = new Account("123", 200);
        Account account2 = new Account("1234", 201);
        List<Account> expectedAccounts = Arrays.asList(account1, account2);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountRepositoryJdbc.findAll();

        assertEquals(expectedAccounts, actualAccounts);
        System.out.println("EXPECTED" + expectedAccounts);
        System.out.println("ACTUAL" + actualAccounts);
    }

    @Test
    void testFindAllNotEmpty(){
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(accountsDummy);
        List<Account> accounts = accountRepositoryJdbc.findAll();
        assertEquals(accountsDummy,accounts);
        assertEquals(accountsDummy.size(), accounts.size());
    }


    @Test
    void testFindById(){
        Account dummyAccount = new Account(1,"001", 123);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt())).thenReturn(dummyAccount);

        Account result = accountRepositoryJdbc.findById(1);
        assertEquals(dummyAccount, result);
        System.out.println("EXPECTED" + dummyAccount);
        System.out.println("ACTUAL" + result);
    }
}