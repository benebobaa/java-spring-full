package com.beneboba.spring_challange.model.request;

import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.util.constraint.CheckPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CheckPassword
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 8, max = 20)
    private String confirmPassword;

    public User toUserEntity(){
        return new User(this.name, this.username, this.password);
    }
}
