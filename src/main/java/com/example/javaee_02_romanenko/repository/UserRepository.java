package com.example.javaee_02_romanenko.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.javaee_02_romanenko.exceptions.UserNotFoundException;
import com.example.javaee_02_romanenko.model.NewUser;
import com.example.javaee_02_romanenko.model.User;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserRepository {

    private static final Map<String, User> USER_DATABASE = initDatabase();

    public User saveNewUser(final NewUser newUser) {
        log.info("Creating new user: {}", newUser);

        final User user =  User.builder()
                .fullName(newUser.getFullName())
                .login(newUser.getLogin())
                .password(newUser.getPassword())
                .build();

        USER_DATABASE.put(user.getLogin(), user);
        return user;
    }

    public User getUserByLogin(final String login) {
        log.info("Get user by login: {}", login);

        return Optional.ofNullable(USER_DATABASE.get(login))
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public boolean isLoginExists(final String login) {
        log.info("Check that user with login: {} exists", login);

        return USER_DATABASE.containsKey(login);
    }

    private static Map<String, User> initDatabase() {
        final Map<String, User> database = new HashMap<>();
        database.put("login1", User.builder().login("login1").password("password1").fullName("fullName1").build());
        database.put("login2", User.builder().login("login2").password("password2").fullName("fullName2").build());
        database.put("login3", User.builder().login("login3").password("password3").fullName("fullName3").build());
        return database;
    }

}