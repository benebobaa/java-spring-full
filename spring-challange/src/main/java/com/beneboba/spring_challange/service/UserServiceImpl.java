package com.beneboba.spring_challange.service;

import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.model.request.LoginRequest;
import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import com.beneboba.spring_challange.model.response.LoginResponse;
import com.beneboba.spring_challange.model.response.UserResponse;
import com.beneboba.spring_challange.repository.UserRepository;
import com.beneboba.spring_challange.security.BCrypt;
import com.beneboba.spring_challange.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public void register(UserRegisterRequest userRegisterRequest) {
        validationService.validate(userRegisterRequest);

        User user = userRepository.findByUsername(userRegisterRequest.getUsername());

        if (user != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        String hashPassword = BCrypt.hashpw(userRegisterRequest.getPassword(), BCrypt.gensalt());

        userRegisterRequest.setPassword(hashPassword);

        userRepository.create(userRegisterRequest.toUserEntity());
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);
        log.info(this.getClass().getName() + " login " + loginRequest);
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }

        if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {

            user.setToken(UUID.randomUUID().toString());
            user.setExpiredAt(Date.next7Days());
            userRepository.updateTokenAndExpiration(user);

            return LoginResponse.builder()
                    .username(user.getUsername())
                    .name(user.getName())
                    .token(user.getToken())
                    .expiredAt(user.getExpiredAt())
                    .build();

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
        }
    }

    @Override
    public UserResponse profile(User user) {
        return userRepository.findById(user.getId()).toUserResponse();
    }
}
