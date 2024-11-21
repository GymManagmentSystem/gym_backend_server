package com.example.payment.service;

import com.example.payment.customResponse.ErrorResponse;
import com.example.payment.customResponse.PaymentClassResponse;
import com.example.payment.customResponse.PaymentResponse;
import com.example.payment.customResponse.SuccessResponse;
import com.example.payment.dto.PaymentDto;
import com.example.payment.model.PaymentModel;
import com.example.payment.repo.PaymentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<PaymentDto> getAllPayments(Integer memberId){
        System.out.println(memberId.getClass().getName());
        try{
            List<PaymentModel> paymentModelList=paymentRepo.getAllPaymentDetailsById(memberId);
            List <PaymentDto> paymentDtoList=modelMapper.map(paymentModelList,new TypeToken<List<PaymentDto>>(){}.getType());
            System.out.println(paymentDtoList);
            return paymentDtoList;
        }catch(Exception e){
            System.out.println(System.out.printf(e.getMessage()));
            throw new RuntimeException("Internal Server Error");
        }
    }

    public ResponseEntity<PaymentClassResponse<PaymentDto>> saveNewPayment(PaymentDto paymentDto){
        System.out.println(paymentDto);
        try{
            paymentRepo.save(modelMapper.map(paymentDto,PaymentModel.class));
            PaymentDto latestPayment=modelMapper.map(paymentRepo.getLatestPayment(),PaymentDto.class);
            System.out.println("latest Payments is "+latestPayment);
            return ResponseEntity.status(HttpStatus.CREATED).body(new PaymentClassResponse<PaymentDto>(latestPayment));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentClassResponse<PaymentDto>(e.getMessage()));
        }

    }
}
