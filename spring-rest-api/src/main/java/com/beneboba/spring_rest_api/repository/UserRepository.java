package com.beneboba.spring_rest_api.repository;

import com.beneboba.spring_rest_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
