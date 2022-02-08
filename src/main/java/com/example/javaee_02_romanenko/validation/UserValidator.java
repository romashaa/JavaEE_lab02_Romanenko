package com.example.javaee_02_romanenko.validation;

import java.util.ArrayList;
import java.util.List;

import com.example.javaee_02_romanenko.exceptions.ConstraintViolationException;
import com.example.javaee_02_romanenko.exceptions.LoginExistsException;
import com.example.javaee_02_romanenko.model.NewUser;
import com.example.javaee_02_romanenko.repository.UserRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateNewUser(final NewUser newUser) {
        if (userRepository.isLoginExists(newUser.getLogin())) {
            throw new LoginExistsException(newUser.getLogin());
        }

        validatePassword(newUser.getPassword());
    }

    private void validatePassword(final String password) {
        final List<String> errors = new ArrayList<>();
        if (password.length() < 3 || password.length() > 7) {
            errors.add("Password has invalid size");
        }

        if (!password.matches("[a-zA-Z0-9]+")) {
            errors.add("Password doesn't match regex");
        }

        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }

}