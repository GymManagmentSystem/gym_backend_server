package com.example.security_service.customResponse;

import lombok.Getter;

@Getter
public class ErrorResponse extends ResponseClass{
    private String error;
    public ErrorResponse(String error) {
        this.error = error;
    }
}
