package com.beneboba.spring_challange.controller;


import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.model.BaseResponse;
import com.beneboba.spring_challange.model.request.LoginRequest;
import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import com.beneboba.spring_challange.model.response.LoginResponse;
import com.beneboba.spring_challange.model.response.UserResponse;
import com.beneboba.spring_challange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            path ="/api/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BaseResponse<String> register(@RequestBody UserRegisterRequest request){
        userService.register(request);
        return BaseResponse.
                <String>builder().
                data("OK").
                build();
    }

    @PostMapping(
            path ="/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = userService.login(request);
        return BaseResponse.
                <LoginResponse>builder().
                data(loginResponse).
                build();
    }

    @GetMapping(
            path = "/api/users/profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BaseResponse<UserResponse> profile(User user){
        return BaseResponse
                .<UserResponse>builder()
                .data(user.toUserResponse())
                .build();
    }
}
