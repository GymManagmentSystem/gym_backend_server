package com.example.exercise.customResponse;

import lombok.Getter;

@Getter
public class ErrorResponse implements ExerciseResponse {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
