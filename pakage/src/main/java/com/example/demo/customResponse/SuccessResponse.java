package com.example.demo.customResponse;


import lombok.Getter;

import java.util.List;

@Getter
public class SuccessResponse <T> implements PackageResponse {
    private  T data;
    private  List<T> dataList;
    public SuccessResponse(T data){
        this.data=data;
    }
    public SuccessResponse(List<T> dataList){
        this.dataList=dataList;
    }


}
