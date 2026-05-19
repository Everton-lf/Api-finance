package br.com.verso.caixa.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDateTime;

@RegisterForReflection
public class ErrorResponse {

    public LocalDateTime timestamp;
    public int status;
    public String error;
    public String message;
    public String path;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse() {
    }
}

