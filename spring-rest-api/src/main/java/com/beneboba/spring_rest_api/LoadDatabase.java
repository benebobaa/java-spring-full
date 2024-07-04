package com.beneboba.spring_rest_api;

import com.beneboba.spring_rest_api.model.Account;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
//
//@Configuration
//public class LoadDatabase {
//    final private AccountRepository accountRepository;
//
//    public LoadDatabase(AccountRepository accountRepo) {
//        this.accountRepository = accountRepo;
//    }
//
//    @Bean
//    public CommandLineRunner initDatabase() {
//        return args -> {
//
//            Account account1 = new Account("1982080185", 1021.99f);
//            Account account2 = new Account("1982032177", 231.50f);
//            Account account3 = new Account("1982094128", 6211.00f);
//
//            accountRepository.saveAll(List.of(account1, account2, account3));
//
//            System.out.println("Sample database initialized.");
//        };
//    }
//}