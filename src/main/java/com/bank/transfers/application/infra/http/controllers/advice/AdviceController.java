package com.bank.transfers.application.infra.http.controllers.advice;

import com.bank.transfers.application.app.exceptions.*;
import com.bank.transfers.application.infra.http.responses.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collection;
import java.util.List;

@ControllerAdvice
public class AdviceController {

    private final MessageSource messageSource;

    public AdviceController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AlreadyUserException.class)
    public ResponseEntity<ErrorResponse> handlerAlreadyUserException(final AlreadyUserException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerBankAccountNotFoundException(final BankAccountNotFoundException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BankAccountNotActiveException.class)
    public ResponseEntity<ErrorResponse> handlerBankAccountNotActiveException(final BankAccountNotActiveException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(LogisticsTransferException.class)
    public ResponseEntity<ErrorResponse> handlerLogisticsTransferException(final LogisticsTransferException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WithoutBalanceException.class)
    public ResponseEntity<ErrorResponse> handlerWithoutBalanceException(final WithoutBalanceException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handlerRuntimeException(final RuntimeException exception) {
        final var response = ErrorResponse.from(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Collection<ErrorResponse>> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final var fields = exception.getBindingResult().getFieldErrors();
        final var errors = getErrorResponses(fields);
        return ResponseEntity.badRequest().body(errors);
    }

    private List<ErrorResponse> getErrorResponses(final Collection<FieldError> fields) {
        return fields.stream()
                .map(field ->
                        ErrorResponse.of(field.getField(), messageSource.getMessage(field, LocaleContextHolder.getLocale()))
                )
                .toList();
    }
}
