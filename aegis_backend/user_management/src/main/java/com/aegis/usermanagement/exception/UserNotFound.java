package com.aegis.usermanagement.exception;


public class UserNotFound extends Exception {
    public UserNotFound(String errorMessage){
     super(errorMessage);
    }
}
