package com.example.exercise.customResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse <T> implements ExerciseResponse {
    private  T data;
    private  List<T> dataList;
    public SuccessResponse(T data){
        this.data=data;
    }
    public SuccessResponse(List<T> dataList){
        this.dataList=dataList;
    }


}
