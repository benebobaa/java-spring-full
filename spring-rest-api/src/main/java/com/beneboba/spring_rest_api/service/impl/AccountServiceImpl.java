package com.beneboba.spring_rest_api.service.impl;

import com.beneboba.spring_rest_api.exception.AccountAlreadyExistsException;
import com.beneboba.spring_rest_api.exception.NoSuchAccountExistsException;
import com.beneboba.spring_rest_api.model.Account;
import com.beneboba.spring_rest_api.model.AccountInfo;
import com.beneboba.spring_rest_api.model.AccountWithBalance;
import com.beneboba.spring_rest_api.repository.AccountRepository;
import com.beneboba.spring_rest_api.repository.AccountRepositoryJdbc;
import com.beneboba.spring_rest_api.repository.impl.TestRepository;
import com.beneboba.spring_rest_api.service.AccountService;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountRepositoryJdbc accountRepositoryJdbc;

    @Autowired
    private TestRepository testRepository;

    public List<Account> listAll() {
        return accountRepository.findAll();
    }

    public Account get(Integer id) {
        try {
            return accountRepository.findById(id).get();
        }catch(NoSuchElementException e) {
            throw new NoSuchAccountExistsException("No Such Account exists!");
        }
    }

    public Account save(Account account){
        try {
            return accountRepository.save(account);
        }catch(DataIntegrityViolationException e) {
            throw new AccountAlreadyExistsException("Account already exists!", e);
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException(e.getConstraintViolations());
        }
    }

    public Account deposit(float amount, Integer id) {
        boolean isExists = accountRepository.existsById(id);

        if(isExists) {
            accountRepository.deposit(amount, id);
            return accountRepository.findById(id).get();
        } else {
            throw new NoSuchAccountExistsException("No Such Account exists!");
        }
    }

    public Account withdraw(float amount, Integer id) {
        boolean isExists = accountRepository.existsById(id);

        if(isExists) {
            accountRepository.withdraw(amount, id);
            return accountRepository.findById(id).get();
        } else {
            throw new NoSuchAccountExistsException("No Such Account exists!");
        }
    }

    public void delete(Integer id) {
        boolean isExists = accountRepository.existsById(id);

        if(isExists) {
            accountRepository.deleteById(id);
        } else {
            throw new NoSuchAccountExistsException("No Such Account exists!");
        }
    }

    //JDBC REPOSITORY

    public List<Account> listAllJdbc() {
        return accountRepositoryJdbc.findAll();
    }

    @Override
    public List<Account> listAllJdbc2() {
        return testRepository.findAll();
    }

    public Account getJdbc(Integer id) {
        return accountRepositoryJdbc.findById(id);
    }

    public Account saveJdbc(Account account){
        logger.info("service account", account);
        int updated = accountRepositoryJdbc.save(account);
        logger.info("service updated", updated);
        if(updated > 0) {
            return account;
        } else {
            throw new AccountAlreadyExistsException("Account already exists!");
        }
    }

    public Account saveJdbc2(Account account){
        logger.info("service account", account);
        int updated = testRepository.save(account);
        logger.info("service updated", updated);
        if(updated > 0) {
            return account;
        } else {
            throw new AccountAlreadyExistsException("Account already exists!");
        }
    }

    public Account depositJdbc(float amount, Integer id) {
        int updated = accountRepositoryJdbc.deposit(amount, id);
        if(updated > 0) {
            return accountRepositoryJdbc.findById(id);
        } else {
            return null;
        }
    }

    public Account withdrawJdbc(float amount, Integer id) {
        int updated = accountRepositoryJdbc.withdraw(amount, id);
        if(updated > 0) {
            return accountRepositoryJdbc.findById(id);
        } else {
            return null;
        }
    }

    public void deleteJdbc(Integer id) {
        accountRepositoryJdbc.deleteById(id);
    }

    @Override
    public List<AccountWithBalance> findAllWithBalance() {
        return accountRepositoryJdbc.findAllWithBalance();
    }


    @Override
    public AccountInfo saveAccountInfoJdbc(AccountInfo accountInfo) {
        int updated = accountRepositoryJdbc.saveAccountInfo(accountInfo);
        logger.info("service updated", updated);
        if(updated > 0) {
            return accountInfo;
        } else {
            throw new AccountAlreadyExistsException("AccountInfo already exists!");
        }
    }


}