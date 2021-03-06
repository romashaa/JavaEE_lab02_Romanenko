package com.example.javaee_02_romanenko.exceptions;

import java.util.List;

import lombok.Getter;

public class ConstraintViolationException extends RuntimeException {

    @Getter
    private final List<String> errors;

    public ConstraintViolationException(final List<String> errors) {
        super("You have errors in you object");
        this.errors = errors;
    }

}