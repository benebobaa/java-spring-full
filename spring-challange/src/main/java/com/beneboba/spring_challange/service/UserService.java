package com.beneboba.spring_challange.service;

import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.model.request.LoginRequest;
import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import com.beneboba.spring_challange.model.response.LoginResponse;
import com.beneboba.spring_challange.model.response.UserResponse;

public interface UserService {
    void register(UserRegisterRequest userRegisterRequest);

    LoginResponse login(LoginRequest loginRequest);

    UserResponse profile(User user);
}
