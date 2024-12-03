package com.example.staffMember.customResponse;


import lombok.Getter;

@Getter
public class ErrorResponse implements StaffMemberResponse{
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
