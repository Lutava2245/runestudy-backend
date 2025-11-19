package com.fatec.runestudy.exception;

import org.springframework.http.HttpStatus;

public class InsufficientCoinsException extends ApiException {

    public InsufficientCoinsException() {
        super("Erro: Moedas insuficientes para esta compra.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.LOCKED;
    }
    
}
