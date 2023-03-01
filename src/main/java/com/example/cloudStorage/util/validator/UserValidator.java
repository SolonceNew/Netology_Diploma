package com.example.cloudStorage.util.validator;


import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.service.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidator implements Validator {

    UserServiceImpl userService;

    @Autowired
    public UserValidator(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User userExist = userService.findByUsername(user.getLogin());
        if(userExist != null) {
            errors.rejectValue("login", "400","user with a such username already exists");
        }

    }
}