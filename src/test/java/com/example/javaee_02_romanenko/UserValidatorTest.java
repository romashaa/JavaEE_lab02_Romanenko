package com.example.javaee_02_romanenko;

import com.example.javaee_02_romanenko.exceptions.ConstraintViolationException;
import com.example.javaee_02_romanenko.exceptions.LoginExistsException;
import com.example.javaee_02_romanenko.model.NewUser;
import com.example.javaee_02_romanenko.model.User;
import com.example.javaee_02_romanenko.repository.UserRepository;
import com.example.javaee_02_romanenko.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserValidator userValidator;

    @Test
    void test(){

        Mockito.when(userRepository.isLoginExists("log")).thenReturn(true);

        assertThatThrownBy(() ->userValidator.validateNewUser(new NewUser("log", "aefa", "efw")))
                .isInstanceOf(LoginExistsException.class);

    }

    @Test
    void invalidPasswordRegexTest() {
        assertThatThrownBy(() -> userValidator.validateNewUser(
                new NewUser("log", "1*ds", "efw")))
                .isInstanceOfSatisfying(
                        ConstraintViolationException.class,
                        ex -> assertThat(ex.getErrors()).isEqualTo(Arrays.asList("Password doesn't match regex")));
    }

    @ParameterizedTest
    @MethodSource("invalidSizePasswords")
    void invalidPasswordSizeTest(String password, List<String> errors) {
        assertThatThrownBy(() -> userValidator.validateNewUser(
                new NewUser("log", password, "efw")))
                .isInstanceOfSatisfying(
                        ConstraintViolationException.class,
                        ex -> assertThat(ex.getErrors()).isEqualTo(errors));
    }
    private static Stream<Arguments> invalidSizePasswords(){
        return Stream.of(
                Arguments.of("b", Arrays.asList("Password has invalid size")),
                Arguments.of("bbbbbbbbbbbbbbbbbb", Arrays.asList("Password has invalid size"))
        );
    }
}
