package com.example.payment.customResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentClassResponse <T>{
    private  T data;
    private List<T> dataList;
    private  String errorMessage;

    public PaymentClassResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public PaymentClassResponse(T data) {
        this.data = data;
    }
    public PaymentClassResponse(List<T> dataList) {
        this.dataList = dataList;
    }
}
