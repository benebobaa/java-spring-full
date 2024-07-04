package com.beneboba.spring_challange.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;

    private String name;

    private String token;

    private Long expiredAt;
}
