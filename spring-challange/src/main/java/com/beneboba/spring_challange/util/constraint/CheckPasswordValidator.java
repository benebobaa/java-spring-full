package com.beneboba.spring_challange.util.constraint;

import com.beneboba.spring_challange.entity.User;
import com.beneboba.spring_challange.model.request.UserRegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckPasswordValidator implements ConstraintValidator<CheckPassword, UserRegisterRequest> {

    @Override
    public boolean isValid(UserRegisterRequest value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.getPassword() == null || value.getConfirmPassword() == null) {
            return true;
        }

        return value.getPassword().equals(value.getConfirmPassword());
    }
}
