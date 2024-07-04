package com.beneboba.spring_rest_api.repository;

import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.AccountInfo;
import com.beneboba.spring_rest_api.model.AccountWithBalance;

import java.util.List;

public interface AccountRepositoryJdbc {
    int save(Account account);
    int update(Account account);
    Account findById(Integer id);
    List<Account> findAll();
    boolean existsById(Integer id);
    int deleteById(Integer id);
    int deposit(float amount, Integer id);
    int withdraw(float amount, Integer id);

    //QUIZ
    List<AccountWithBalance> findAllWithBalance();


    int saveAccountInfo(AccountInfo accountInfo);
}
