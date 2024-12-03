package com.example.staffMember.customResponse;


import lombok.Getter;

import java.util.List;

@Getter
public class SuccessResponse <T> implements StaffMemberResponse {
    private  T data;
    private  List<T> dataList;
    public SuccessResponse(T data){
        this.data=data;
    }
    public SuccessResponse(List<T> dataList){
        this.dataList=dataList;
    }


}
