package com.example.exercise.exception; // Use the appropriate package based on your structure

import com.example.exercise.customResponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.StringJoiner;

@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("inside the global exception");
        StringJoiner errorMessage = new StringJoiner("; ", "Validation errors: ", "");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
    }
}

