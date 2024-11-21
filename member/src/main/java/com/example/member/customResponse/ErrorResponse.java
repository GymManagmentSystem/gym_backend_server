package com.example.member.customResponse;

import lombok.Getter;

@Getter
public class ErrorResponse implements MemberResponse {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
