package com.beneboba.spring_challange.entity;


import com.beneboba.spring_challange.model.response.UserResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String name;

    private String password;

    private String token;

    private Long expiredAt;

    public User(String name, String username, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public UserResponse toUserResponse(){
        return new UserResponse(this.username, this.name, this.token, this.expiredAt);
    }
}
