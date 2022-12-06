package com.example.hodolog.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends MasterException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    private String fieldName;
    private String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        this.fieldName = fieldName;
        this.message =  message;
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
