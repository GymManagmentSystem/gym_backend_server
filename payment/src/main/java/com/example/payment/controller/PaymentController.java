package com.example.payment.controller;


import com.example.payment.customResponse.PaymentClassResponse;
import com.example.payment.customResponse.PaymentResponse;
import com.example.payment.dto.PaymentDto;
import com.example.payment.service.PaymentService;
import jakarta.persistence.GeneratedValue;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/{memberId}")
    public ResponseEntity<PaymentClassResponse<PaymentDto>> getAllPaymentsById(@PathVariable Integer memberId){
        System.out.println("Payment controller is called");
        try{
            List<PaymentDto> paymentList=paymentService.getAllPayments(memberId);
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentClassResponse<PaymentDto>(paymentList));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentClassResponse<PaymentDto>("Internal Server Error"));
        }


    }

    @PostMapping(value = "/{memberId}")
    public ResponseEntity<PaymentClassResponse<PaymentDto>> saveNewPayment(@RequestBody PaymentDto paymentDto){
        System.out.println(paymentDto.getPaymentAmount());
        return paymentService.saveNewPayment(paymentDto);
    }

    @GetMapping(value="/paymentStatusCount")
    public ResponseEntity<PaymentResponse> getPaymentStatusCount(){
        return paymentService.getCurrentExpiredMembersCount();
    }

    @GetMapping(value = "/latest")
    public ResponseEntity<PaymentResponse> getLatestPayments(){
        return paymentService.getLatestPayments();
    }

    @GetMapping(value="/{packageType}/count")
    public ResponseEntity<PaymentResponse> getMonthlyPackageCount(@PathVariable String packageType){
        return paymentService.getMonthlyPackageCount(packageType);
    }

    @GetMapping(value="/monthlyIncome")
    public ResponseEntity<PaymentResponse> getMonthlyIncome(){
        return paymentService.getMontlyIncome();
    }

}
