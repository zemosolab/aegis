package com.aegis.devicemanagement.exception.user;

public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String error){
        super(error);
    }
}
