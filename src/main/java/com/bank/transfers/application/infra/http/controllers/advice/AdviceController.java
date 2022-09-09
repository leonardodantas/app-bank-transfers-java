package com.bank.transfers.application.infra.http.controllers.advice;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(AlreadyUserException.class)
    public ResponseEntity<ErrorResponse> handlerAlreadyUserException(final AlreadyUserException exception){
        final var response = ErrorResponse.from(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
