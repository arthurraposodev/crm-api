package com.am.crm.customer.web;

import com.am.crm.customer.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        return buildErrorResponse("Invalid JSON input", HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, List.of("Resource not found"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, List.of("Invalid request data"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, WebRequest request) {
        return buildErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return buildErrorResponse("Validation Failed", HttpStatus.BAD_REQUEST, validationErrors);
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format("Field '%s': %s (rejected value: %s)", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status, List<String> details) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(status.value());
        errorResponse.setDetails(details);
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(OffsetDateTime.now());
        return new ResponseEntity<>(errorResponse, status);
    }
}
