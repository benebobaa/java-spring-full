package com.beneboba.spring_challange.repository;

import com.beneboba.spring_challange.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AssertTrueValidator assertTrueValidator;

    @Override
    public void create(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (username, name, password) VALUES (?, ?, ?)",
                    user.getUsername(), user.getName(), user.getPassword());
    }

    @Override
    public User findById(Integer id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                BeanPropertyRowMapper.newInstance(User.class),
                id
        );
    }

    @Override
    public User findByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE username = ?",
                    BeanPropertyRowMapper.newInstance(User.class),
                    username
            );
        } catch (DataAccessException e){
            log.error(this.getClass().getName() + " findByUsername " + e.toString());
            return null;
        }
    }

    @Override
    public void updateTokenAndExpiration(User user) {
        jdbcTemplate.update(
                "UPDATE users SET token = ?, expired_at = ? WHERE id = ?",
                user.getToken(), user.getExpiredAt(), user.getId()
        );
    }

    @Override
    public User findByToken(String token) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE token = ?",
                BeanPropertyRowMapper.newInstance(User.class),
                token
        );
    }
}
