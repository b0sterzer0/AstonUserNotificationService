package com.aston.user_service.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("User with id " + id + " not found");
    }
}
