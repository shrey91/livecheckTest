package com.liverton.livecheck.validator;

import com.liverton.livecheck.dao.model.User;
import com.liverton.livecheck.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sshah on 16/08/2016.
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass){
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors){
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","NotEmpty");
        if(user.getName().length() <6 || user.getName().length() > 32){
            errors.rejectValue("name", "Size.userForm.name");
        }
        if (userService.findByName(user.getName()) != null){
            errors.rejectValue("name","Duplicate.userForm.name");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","NotEmpty");
        if(user.getPassword().length() < 6 || user.getPassword().length()>32){
            errors.rejectValue("password","Size.userForm.password");
        }

    }
}
