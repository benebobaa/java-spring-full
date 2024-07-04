package com.beneboba.spring_rest_api.repository.impl;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.AccountInfo;
import com.beneboba.spring_rest_api.model.AccountWithBalance;
import com.beneboba.spring_rest_api.repository.AccountRepositoryJdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepositoryJdbcImpl implements AccountRepositoryJdbc {
    public AccountRepositoryJdbcImpl(){}

    public AccountRepositoryJdbcImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate =  jdbcTemplate;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Account account) {
        return jdbcTemplate.update("INSERT INTO accounts (account_number, balance) VALUES(?,?)",
                new Object[] { account.getAccountNumber(), account.getBalance() });
    }

    @Override
    public int update(Account account) {
        return jdbcTemplate.update("UPDATE accounts SET account_number=?, balance=? WHERE id=?",
                new Object[] { account.getAccountNumber(), account.getBalance(), account.getId() });
    }

    @Override
    public Account findById(Integer id) {
        return jdbcTemplate.queryForObject("SELECT * FROM accounts WHERE id=?",
                BeanPropertyRowMapper.newInstance(Account.class), id);
    }

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query("SELECT * from accounts", BeanPropertyRowMapper.newInstance(Account.class));
    }

    @Override
    public boolean existsById(Integer id) {
        Integer count = this.jdbcTemplate.queryForObject("select count(*) from accounts WHERE id=?", Integer.class, id);
        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM accounts WHERE id=?", id);
    }

    @Override
    public int deposit(float amount, Integer id) {
        return jdbcTemplate.update("UPDATE accounts SET balance=balance + ? WHERE id=?",
                new Object[] { amount, id });
    }

    @Override
    public int withdraw(float amount, Integer id) {
        return jdbcTemplate.update("UPDATE accounts SET balance=balance - ? WHERE id=?",
                new Object[] { amount, id });
    }


    @Override
    public List<AccountWithBalance> findAllWithBalance() {
        return jdbcTemplate.query("SELECT\n" +
                "    a.id,\n" +
                "    ai.account_name,\n" +
                "    a.balance\n" +
                "FROM\n" +
                "    accounts a JOIN account_info ai on a.id = ai.account_id", BeanPropertyRowMapper.newInstance(AccountWithBalance.class));
    }

    @Override
    public int saveAccountInfo(AccountInfo accountInfo) {
        return jdbcTemplate.update("INSERT INTO account_info (account_id, account_name) VALUES(?,?)",
                new Object[] { accountInfo.getAccountId(), accountInfo.getAccountName() });
    }
}