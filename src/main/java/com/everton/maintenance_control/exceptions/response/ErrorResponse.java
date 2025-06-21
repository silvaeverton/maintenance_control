package com.everton.maintenance_control.exceptions.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;


    public ErrorResponse(LocalDateTime timestamp, Integer status, String message, String path) {

        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;

    }
}
