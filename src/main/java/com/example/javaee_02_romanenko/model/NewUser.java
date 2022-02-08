package com.example.javaee_02_romanenko.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

@Value
//@Builder
public class NewUser {

    public NewUser(String login, String password, String fullName) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
    }

    final String login;
    private String password;
    private String fullName;

}