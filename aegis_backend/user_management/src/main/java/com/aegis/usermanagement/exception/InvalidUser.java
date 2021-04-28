package com.aegis.usermanagement.exception;

public class InvalidUser extends Exception {
    public InvalidUser(String errorMessage){
        super(errorMessage);
    }
}
