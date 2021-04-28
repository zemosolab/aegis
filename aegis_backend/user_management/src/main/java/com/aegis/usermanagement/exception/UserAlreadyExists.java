package com.aegis.usermanagement.exception;

public class UserAlreadyExists extends Exception{
    public UserAlreadyExists(String  errorMessage){
        super(errorMessage);
    }
}
