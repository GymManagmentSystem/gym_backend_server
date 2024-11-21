package com.example.demo.customResponse;

import lombok.Getter;

@Getter
public class ErrorResponse implements PackageResponse {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
