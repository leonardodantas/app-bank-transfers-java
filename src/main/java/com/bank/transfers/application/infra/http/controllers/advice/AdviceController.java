package com.bank.transfers.application.infra.http.controllers.advice;

import com.bank.transfers.application.app.exceptions.AlreadyUserException;
import com.bank.transfers.application.app.exceptions.BankAccountNotFoundException;
import com.bank.transfers.application.infra.http.responses.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;

@ControllerAdvice
public class AdviceController {

    private final MessageSource messageSource;

    public AdviceController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AlreadyUserException.class)
    public ResponseEntity<ErrorResponse> handlerAlreadyUserException(final AlreadyUserException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerBankAccountNotFoundException(final BankAccountNotFoundException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final var fields = exception.getBindingResult().getFieldErrors();

        final var errors = fields.stream()
                .map(field ->
                        ErrorResponse.of(field.getField(), messageSource.getMessage(field, LocaleContextHolder.getLocale()))
                )
                .toList();

        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }
}
