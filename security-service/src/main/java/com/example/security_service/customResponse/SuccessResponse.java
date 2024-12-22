package com.example.security_service.customResponse;

import lombok.Getter;

@Getter
public class SuccessResponse extends ResponseClass {
    private String successMessage;
    private String token;
    public SuccessResponse(String success,String token) {
        this.successMessage = success;
        this.token = token;
    }
}
