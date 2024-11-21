package com.example.payment.customResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data

@NoArgsConstructor
public class ErrorResponse implements PaymentResponse {
    private  String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
