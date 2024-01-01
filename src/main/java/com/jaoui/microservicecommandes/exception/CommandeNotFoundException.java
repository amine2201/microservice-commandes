package com.jaoui.microservicecommandes.exception;

public class CommandeNotFoundException extends RuntimeException{
    public CommandeNotFoundException(String s) {
        super(s);
    }
}
