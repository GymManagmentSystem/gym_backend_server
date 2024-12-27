package com.example.payment.service;

import com.example.payment.customResponse.ErrorResponse;
import com.example.payment.customResponse.PaymentClassResponse;
import com.example.payment.customResponse.PaymentResponse;
import com.example.payment.customResponse.SuccessResponse;
import com.example.payment.dto.PaymentDto;
import com.example.payment.dto.PaymentMonthDto;
import com.example.payment.model.PaymentModel;
import com.example.payment.repo.PaymentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<PaymentClassResponse<PaymentDto>> saveNewPayment(PaymentDto paymentDto){
        System.out.println(paymentDto);

        System.out.println("Issue in 1 block");
        try{
            //this will check payments has been done before by the member and fresh member first payment is handled here
            if(!paymentRepo.existsById(paymentDto.getMemberId())){
                System.out.println("Issue in 2 block");
                paymentRepo.save(modelMapper.map(paymentDto,PaymentModel.class));
                PaymentDto latestPayment=modelMapper.map(paymentRepo.getLatestPayment(),PaymentDto.class);
                System.out.println("latest Payments is "+latestPayment);
                return ResponseEntity.status(HttpStatus.OK).body(new PaymentClassResponse<PaymentDto>(latestPayment));
            }

            //this will invalidate the old payment done by the user
            Integer isInvalidatedPastPayments=paymentRepo.invalidatePastPayment(paymentDto.getMemberId());
            //if the updation is failed this will throw an error
            System.out.println("Issue in 3 block");
            if(isInvalidatedPastPayments==0){
                throw new RuntimeException("PIF"); //payment invalidation failed
            }
            System.out.println("Issue in 4 block");
            paymentRepo.save(modelMapper.map(paymentDto,PaymentModel.class));
            System.out.println("Issue in 5 block");
            PaymentDto latestPayment=modelMapper.map(paymentRepo.getLatestPayment(),PaymentDto.class);
            System.out.println("latest Payments is "+latestPayment);
            System.out.println("Issue in 6 block");
            return ResponseEntity.status(HttpStatus.OK).body(new PaymentClassResponse<PaymentDto>(latestPayment));
        }catch(Exception e){
            System.out.println(System.out.printf(e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentClassResponse<PaymentDto>(e.getMessage()));
        }

    }

    public ResponseEntity<PaymentResponse> getCurrentExpiredMembersCount(){
        try{
            Integer currentMemberCount=paymentRepo.getCurrentMemberCount();
            Integer expiredMemberCount=paymentRepo.getExpiredMemberCount();
            List<Integer> memberStatusCount= Arrays.asList(currentMemberCount,expiredMemberCount);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<Integer>(memberStatusCount));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }
    }

    public ResponseEntity<PaymentResponse> getLatestPayments(){
        try{
            List<PaymentModel> paymentModelList=paymentRepo.getLatestPayments();
            List <PaymentDto> paymentDtoList=modelMapper.map(paymentModelList,new TypeToken<List<PaymentDto>>(){}.getType());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<PaymentDto>(paymentDtoList));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }
    }

    public ResponseEntity<PaymentResponse> getMonthlyPackageCount(String packageType){
        try{
            List<Object[]> payementMonthList=paymentRepo.getMonthlyPackageCount(packageType);
            List<PaymentMonthDto> paymentMonthDtoList= payementMonthList.stream().map(record->{
                int memberCount=((Number)record[0]).intValue();
                int month=((Number)record[1]).intValue();
                int year=((Number)record[2]).intValue();
                return new PaymentMonthDto(memberCount,month,year);
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<PaymentMonthDto>(paymentMonthDtoList));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }
    }
}
