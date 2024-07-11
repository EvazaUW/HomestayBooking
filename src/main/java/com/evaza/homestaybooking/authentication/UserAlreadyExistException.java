package com.evaza.homestaybooking.authentication;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("Username already exists");
    }
}
