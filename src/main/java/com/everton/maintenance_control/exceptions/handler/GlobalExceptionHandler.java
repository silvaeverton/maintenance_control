package com.everton.maintenance_control.exceptions.handler;

import com.everton.maintenance_control.exceptions.custom.AlreadyModifiedException;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.exceptions.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponse> handlerNotFound(NotFoundException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AlreadyModifiedException.class)
    ResponseEntity<ErrorResponse> alreadyModifiedException(AlreadyModifiedException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handlerGenericException(Exception ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
