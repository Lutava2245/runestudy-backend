package com.fatec.runestudy.exception;

public abstract class ApiException extends RuntimeException {
    
    public abstract org.springframework.http.HttpStatus getStatus();

    public ApiException(String message) {
        super(message);
    }
    
}