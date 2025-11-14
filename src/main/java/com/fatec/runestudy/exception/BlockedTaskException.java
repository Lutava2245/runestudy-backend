package com.fatec.runestudy.exception;

import org.springframework.http.HttpStatus;

public class BlockedTaskException extends ApiException {

    public BlockedTaskException() {
        super("Erro: Tarefa está bloqueada.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.LOCKED;
    }
    
}
