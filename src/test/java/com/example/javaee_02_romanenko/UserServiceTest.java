package com.example.javaee_02_romanenko;

import com.example.javaee_02_romanenko.exceptions.LoginExistsException;
import com.example.javaee_02_romanenko.exceptions.UserNotFoundException;
import com.example.javaee_02_romanenko.model.NewUser;
import com.example.javaee_02_romanenko.model.User;
import com.example.javaee_02_romanenko.repository.UserRepository;
import com.example.javaee_02_romanenko.service.UserService;
import com.example.javaee_02_romanenko.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class UserServiceTest {

    @SpyBean
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void NewUserAddingTest(){
        userService.createNewUser(new NewUser("log","add","wdw"));

        Mockito.verify(userValidator).validateNewUser(ArgumentMatchers.any());
        assertThat(userRepository.isLoginExists("log")).isTrue();
    }

    @Test
    void getUserByLoginTest() {

        NewUser user = new NewUser("login","abc","user");
        userRepository.saveNewUser(user);

        assertThat(userService.getUserByLogin("login"))
                .returns("login", User::getLogin)
                .returns("abc", User::getPassword)
                .returns("user", User::getFullName);
    }

    @Test
    void getNotExistingUserTest() {
        assertThatThrownBy(() -> userService.getUserByLogin("abc"))
                .isInstanceOfSatisfying(
                        UserNotFoundException.class,
                        ex -> assertThat(ex.getMessage()).isEqualTo("Can't find user by login: abc"));
    }


    @Test
    void sameLoginExceptionTest() {
        NewUser user = new NewUser("login","abc","user");
        userRepository.saveNewUser(user);

        assertThatThrownBy(() -> userService.createNewUser(user))
                .isInstanceOfSatisfying(LoginExistsException.class,
                        ex -> assertThat(ex.getMessage()).isEqualTo("Login %s already taken", user.getLogin()));
    }

}


