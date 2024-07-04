package com.beneboba.spring_challange.service;


import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import com.beneboba.spring_challange.security.BCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private ValidationService validationService;

    private UserRegisterRequest userRegisterRequest;

    @BeforeEach
    void setUp(){
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUsername("beneboba");
        userRegisterRequest.setName("bene");
        userRegisterRequest.setPassword("testing");
        userRegisterRequest.setConfirmPassword("testing");

        String hashPassword = BCrypt.hashpw(userRegisterRequest.getPassword(), BCrypt.gensalt());
        userRegisterRequest.setPassword(hashPassword);
    }

    @Test
    void testValidateThrowsException() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        violations.add(mock(ConstraintViolation.class));

        when(validator.validate((Object) userRegisterRequest)).thenReturn(violations);

        assertThrows(ConstraintViolationException.class,
                () -> validationService.validate(userRegisterRequest)
        );

        verify(validator).validate(userRegisterRequest);
    }

    @Test
    void testValidateEmptyViolations() {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        when(validator.validate((Object) userRegisterRequest)).thenReturn(violations);

        assertDoesNotThrow(
                () -> validationService.validate(userRegisterRequest)
        );

        verify(validator).validate(userRegisterRequest);
    }

}
