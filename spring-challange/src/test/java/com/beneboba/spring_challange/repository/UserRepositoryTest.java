package com.beneboba.spring_challange.repository;

import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setName("bene");
        user.setUsername("beneboba");
        user.setPassword("bene123");
    }

    @Test
    void testCreateUserAndFindByUsername(){
        userRepository.create(user);
        verify(jdbcTemplate).update(anyString(), any(Object[].class));

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenReturn(user);
        User result = userRepository.findByUsername(user.getUsername());

        assertEquals(user, result);
    }

    @Test
    void testFindByUsernameThrowsDataAccessException() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenThrow(new DataAccessException("Test exception") {});

        User result = userRepository.findByUsername("testUsername");

        assertNull(result);
        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    void testFindById(){
        user.setId(1);

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(user);
        User result = userRepository.findById(user.getId());

        assertEquals(user, result);
    }

    @Test
    void testUpdateTokenAndExpiration(){
        user.setToken(UUID.randomUUID().toString());
        user.setExpiredAt(Date.next7Days());

        userRepository.updateTokenAndExpiration(user);
        verify(jdbcTemplate).update(anyString(), any(Object[].class));
    }

    @Test
    void testFindByToken(){
        user.setToken(UUID.randomUUID().toString());

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString()))
                .thenReturn(user);
        User result = userRepository.findByToken(user.getToken());

        assertEquals(user, result);
    }
}
