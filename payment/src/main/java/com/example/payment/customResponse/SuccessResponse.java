package com.example.payment.customResponse;


import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class SuccessResponse <T> implements PaymentResponse {
    private  T data;
    private  List<T> dataList;
    public SuccessResponse(T data){
        this.data=data;
    }
    public SuccessResponse(List<T> dataList){
        this.dataList=dataList;
    }


}
