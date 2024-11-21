package com.example.member.controller;


import com.example.member.customResponse.ErrorResponse;
import com.example.member.customResponse.MemberResponse;
import com.example.member.customResponse.SuccessResponse;
import com.example.member.dto.BasicMemberDto;
import com.example.member.dto.MemberDetailsDto;
import com.example.member.dto.MemberDto;
import com.example.member.dto.MemberRegistrationDto;
import com.example.member.service.MemberService;
import com.example.payment.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public ResponseEntity<? extends MemberResponse> getMembers(){
        try{
            BasicMemberDto memberDetails=memberService.getAllMembers();
            return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<BasicMemberDto>(memberDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }

    }

    @PostMapping("/")
    public ResponseEntity<MemberResponse> addNewMember(@RequestBody MemberRegistrationDto memberRegistrationDto){
        MemberDto memberDt0=new MemberDto();
        memberDt0.setFirstName(memberRegistrationDto.getFirstName());
        memberDt0.setLastName(memberRegistrationDto.getLastName());
        memberDt0.setEmail(memberRegistrationDto.getEmail());
        memberDt0.setAddress( memberRegistrationDto.getAddress() );
        memberDt0.setAge(memberRegistrationDto.getAge());
        memberDt0.setGender(memberRegistrationDto.getGender());
        memberDt0.setHeight(memberRegistrationDto.getHeight());
        memberDt0.setWeight(memberRegistrationDto.getWeight());
        memberDt0.setContactNumber(memberRegistrationDto.getContactNumber());
        memberDt0.setDateRegistered(memberRegistrationDto.getDateRegistered());

        PaymentDto paymentDto=new PaymentDto();
        paymentDto.setPaymentDate(memberRegistrationDto.getPaymentDate());
        paymentDto.setPaymentTime(memberRegistrationDto.getPaymentTime());
        paymentDto.setValidity(memberRegistrationDto.isPaymentValidity());
        paymentDto.setPackageType(memberRegistrationDto.getPackageType());


        try{
            MemberRegistrationDto savedMember= memberService.addNewMember(memberDt0,paymentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<MemberRegistrationDto>(savedMember));
        }catch(Exception e){
            if(e.getMessage().equals("NE")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Name Already Exists"));
            }
            if(e.getMessage().equals("EE")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Email Already Exists"));
            }
            if(e.getMessage().equals("ME")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Mobile Number Already Exists"));
            }
            if(e.getMessage().equals("PF")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Payment failed Try Again"));
            }
            if(e.getMessage().equals("DIVE")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Data Integrity Violation"));
            }
            if(e.getMessage().equals("RF")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Request Failed Try Again"));
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Something went wrong"));
            }

        }


    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberDetailsById(@PathVariable("memberId") String memberId){
        try{
            MemberDetailsDto memberDetails=memberService.getMemberDetails(Integer.parseInt(memberId));
            return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<MemberDetailsDto>(memberDetails));
        }catch(Exception e){
            if(e.getMessage().equals("MNF")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Member Not Found"));
            }if(e.getMessage().equals("RF")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Request Failed Try Again"));
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
            }

        }
    }

    @PutMapping("/")
    public ResponseEntity<? extends MemberResponse> updateMember(@RequestBody MemberDto memberDto) {
        try {
            MemberDto updatedMember = memberService.updateMember(memberDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<MemberDto>(updatedMember));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().equals("MNF")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Member Updation Failed"));
            }
            if (e.getMessage().equals("NE")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Name Already Exists"));
            }
            if (e.getMessage().equals("EE")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Email Already Exists"));
            }
            if (e.getMessage().equals("ME")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Mobile Number Already Exists"));
            }
            if (e.getMessage().equals("NUMF")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No Updated Member Found"));
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
            }
        }

    }


}
