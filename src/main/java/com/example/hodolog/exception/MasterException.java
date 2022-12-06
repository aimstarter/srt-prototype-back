package com.example.hodolog.exception;

public abstract class MasterException extends RuntimeException {

    public MasterException(String message) {
        super(message);
    }

    public MasterException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
