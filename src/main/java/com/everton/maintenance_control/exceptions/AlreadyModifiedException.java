package com.everton.maintenance_control.exceptions;

import lombok.Getter;

@Getter
public class AlreadyModifiedException extends RuntimeException {
    private final Integer status;


    public AlreadyModifiedException(final String message, final Integer status) {
        super(message);
        this.status = status;
    }
}
