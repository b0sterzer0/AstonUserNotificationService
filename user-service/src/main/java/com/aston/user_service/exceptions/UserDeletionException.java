package com.aston.user_service.exceptions;

public class UserDeletionException extends RuntimeException {
    public UserDeletionException(long id) {
        super("Can not delete User with id " + id);
    }
}
