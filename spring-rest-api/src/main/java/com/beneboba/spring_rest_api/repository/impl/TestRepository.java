package com.beneboba.spring_rest_api.repository.impl;


import com.beneboba.spring_rest_api.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Account account) {
        return jdbcTemplate.update("INSERT INTO accounts (account_number, balance) VALUES(?,?)",
                new Object[] { account.getAccountNumber(), account.getBalance() });
    }

    public List<Account> findAll() {
        return jdbcTemplate.query("SELECT * from accounts", BeanPropertyRowMapper.newInstance(Account.class));
    }
}
