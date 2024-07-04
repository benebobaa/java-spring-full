package com.beneboba.spring_challange.service;


import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import com.beneboba.spring_challange.repository.UserRepository;
import com.beneboba.spring_challange.security.BCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserServiceImpl userService;

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
    void testRegisterSuccess(){
        String hashPassword = BCrypt.hashpw(userRegisterRequest.getPassword(), BCrypt.gensalt());

        userRegisterRequest.setPassword(hashPassword);

        userService.register(userRegisterRequest);

        verify(validationService, times(1)).validate(userRegisterRequest);
        verify(userRepository, times(1)).findByUsername(userRegisterRequest.getUsername());
        verify(userRepository, times(1)).create(userRegisterRequest.toUserEntity());
    }

    @Test
    void testRegisterFailedUsernameAlreadyExists(){
        User existingUser = new User();
        existingUser.setUsername(userRegisterRequest.getUsername());

        when(userRepository.findByUsername(userRegisterRequest.getUsername())).thenReturn(existingUser);

        assertThrows(ResponseStatusException.class, () -> {
            userService.register(userRegisterRequest);
        });
        verify(userRepository, times(1)).findByUsername(userRegisterRequest.getUsername());
        verify(userRepository, times(0)).create(userRegisterRequest.toUserEntity());
    }

    @Test
    void testRegisterFailedValidation(){
        userRegisterRequest.setUsername("");

        doThrow(new ConstraintViolationException(new HashSet<>())).when(validationService).validate(userRegisterRequest);

        assertThrows(ConstraintViolationException.class, () -> {
            userService.register(userRegisterRequest);
        });
        verify(userRepository, times(0)).findByUsername(userRegisterRequest.getUsername());
        verify(userRepository, times(0)).create(userRegisterRequest.toUserEntity());
    }

//    @Test
//    void testValidateThrowsException() {
//        Set<ConstraintViolation<Object>> violations = new HashSet<>();
//        violations.add(mock(ConstraintViolation.class));
//
//        when(validator.validate((Object) userRegisterRequest)).thenReturn(violations);
//
//        assertThrows(ConstraintViolationException.class,
//                () -> validationService.validate(userRegisterRequest)
//        );
//
//        verify(validator).validate(userRegisterRequest);
//    }
}
