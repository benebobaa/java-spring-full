package com.beneboba.spring_rest_api.service.impl;

import com.beneboba.spring_rest_api.model.User;
import com.beneboba.spring_rest_api.repository.UserRepository;
import com.beneboba.spring_rest_api.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(String userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }
}
