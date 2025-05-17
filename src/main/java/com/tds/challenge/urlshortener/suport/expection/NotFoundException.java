package com.tds.challenge.urlshortener.suport.expection;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
