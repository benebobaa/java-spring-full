package com.beneboba.spring_postgres.repository;
import com.beneboba.spring_postgres.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.name = ?1 and u.email = ?2")
    List<User> findUserUsingQuery(String name, String email);

    @Query(value = "SELECT * FROM Users u WHERE u.name = ?1 and u.email = ?2", nativeQuery = true)
    List<User> findUserUsingNativeQuery(String name, String email);

    @Query(value = "SELECT * FROM Users u WHERE u.email like %?1%", nativeQuery = true)
    List<User> findUserUsingNativeQueryLike(String email);

    @Query("SELECT u FROM User u")
    List<User> findAllUsers(Sort sort);

    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAllReverse();

}