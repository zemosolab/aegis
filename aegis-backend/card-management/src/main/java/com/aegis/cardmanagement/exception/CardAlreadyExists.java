package com.aegis.cardmanagement.exception;

public class CardAlreadyExists extends Exception {
    public CardAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
