package com.beneboba.spring_rest_api.repository;

import com.beneboba.spring_rest_api.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void cleanTables(){
        accountRepository.deleteAll();
        Account account = new Account("123", 200);
        Account account2 = new Account("111", 2000);
        accountRepository.save(account);
        accountRepository.save(account2);
    }

    @Test
    public void testCreateReadDelete() {
        String accountNumber = "1111122222";
        float balance = 20000;
        Account account = new Account(accountNumber,balance);
        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount);
        assertNotNull(savedAccount.getId());
        assertThat(savedAccount).usingRecursiveComparison().ignoringFields("id").isEqualTo(account);

        Iterable<Account> accounts = accountRepository.findAll();
        assertThat(accounts).extracting(Account::getAccountNumber).containsOnlyOnce(accountNumber);
        accountRepository.deleteAll();
        assertThat(accountRepository.findAll().isEmpty());
    }

    @Test
    public void testGetAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        assertNotNull(accounts);
        assertEquals(accounts.size(), 2);
    }



}