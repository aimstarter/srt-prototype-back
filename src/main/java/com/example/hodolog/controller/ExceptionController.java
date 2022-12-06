package com.example.hodolog.controller;

import com.example.hodolog.exception.InvalidRequest;
import com.example.hodolog.exception.MasterException;
import com.example.hodolog.exception.PostNotFound;
import com.example.hodolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        log.error("Enter Exception Controller Advice.", e);
//        if(e.hasErrors()) {
//            return new ErrorResponse("400", "잘못된 요청입니다.");
//        } else {
//            return new ErrorResponse("400", "잘못된 요청입니다.");
//        }
        // ErrorResponse errorResponse = new ErrorResponse("400", "잘못된 요청입니다.");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String message = fieldError.getDefaultMessage();
//
//        Map<String, String> response = new HashMap<>();
//        response.put(field, message);
        return errorResponse;
    }

    // @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MasterException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> masterException(MasterException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        if(e instanceof InvalidRequest) {
            InvalidRequest invalidRequest = (InvalidRequest) e;
            String fieldName = invalidRequest.getFieldName();
            String message = invalidRequest.getMessage();

            errorResponse.addValidation(fieldName, message);
        }

        ResponseEntity<ErrorResponse> errorResponseEntity = ResponseEntity.status(statusCode).body(errorResponse);

        return errorResponseEntity;
    }
}
