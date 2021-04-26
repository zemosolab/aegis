package com.aegis.cardmanagement.exception;

public class CardNotFound extends  Exception {
    public CardNotFound(String errorMessage){
        super(errorMessage);
    }
}
