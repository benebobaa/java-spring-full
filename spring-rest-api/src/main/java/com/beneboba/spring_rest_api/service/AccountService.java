package com.beneboba.spring_rest_api.service;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.AccountInfo;
import com.beneboba.spring_rest_api.model.AccountWithBalance;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountService {
    List<Account> listAll();

    Account get(Integer id);

    Account save(Account account);

    Account deposit(float amount, Integer id);

    Account withdraw(float amount, Integer id);

    void delete(Integer id);

    //JDBC

    List<Account> listAllJdbc();
    List<Account> listAllJdbc2();

    Account getJdbc(Integer id);

    Account saveJdbc(Account account);

    Account saveJdbc2(Account account);

    Account depositJdbc(float amount, Integer id);

    Account withdrawJdbc(float amount, Integer id);

    void deleteJdbc(Integer id);

    List<AccountWithBalance> findAllWithBalance();



    AccountInfo saveAccountInfoJdbc(AccountInfo accountInfo);

}