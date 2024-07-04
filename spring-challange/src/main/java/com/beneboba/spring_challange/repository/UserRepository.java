package com.beneboba.spring_challange.repository;

import com.beneboba.spring_challange.entity.User;

public interface UserRepository {
    void create(User user);

    User findById(Integer id);

    User findByUsername(String username);

    void updateTokenAndExpiration(User user);

    User findByToken(String token);
}
