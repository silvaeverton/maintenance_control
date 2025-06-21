package com.everton.maintenance_control.exceptions.handler;

import com.everton.maintenance_control.exceptions.custom.AlreadyModifiedException;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.exceptions.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    private HttpServletRequest request;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void shouldHandleNotFound() {
        NotFoundException ex = mock(NotFoundException.class);
        when(ex.getMessage()).thenReturn("Resource not found");
        when(ex.getStatus()).thenReturn(404);
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = handler.handlerNotFound(ex,request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("/api/test", response.getBody().getPath());

    }

    @Test
    void shouldAlreadyModifiedException() {

        AlreadyModifiedException ex = mock(AlreadyModifiedException.class);
        when(ex.getMessage()).thenReturn("Order already closed");
        when(ex.getStatus()).thenReturn(409);
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = handler.alreadyModifiedException(ex,request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Order already closed", response.getBody().getMessage());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("/api/test", response.getBody().getPath());



    }

    @Test
    void shouldHandlerGenericException() {

        Exception exception = new RuntimeException("Erro genérico");
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = handler.handlerGenericException(exception,request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro genérico", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("/api/test", response.getBody().getPath());

    }
}