package com.everton.maintenance_control.exceptions.custom;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Integer status;

    public NotFoundException(final String message, final Integer status) {

        super(message);
        this.status = status;
    }
}
