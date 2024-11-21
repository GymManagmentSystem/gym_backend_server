package com.example.schedule.customResponse;

import lombok.Getter;

@Getter
public class ErrorResponse implements ScheduleResponse {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
