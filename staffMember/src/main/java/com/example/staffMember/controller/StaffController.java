package com.example.staffMember.controller;


import com.example.staffMember.customResponse.ErrorResponse;
import com.example.staffMember.customResponse.StaffMemberResponse;
import com.example.staffMember.customResponse.SuccessResponse;
import com.example.staffMember.dto.StaffMemberDto;
import com.example.staffMember.service.StaffMemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/staff/members")
public class StaffController {

    @Autowired
    private StaffMemberService staffMemberService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping( "/")
    public ResponseEntity<StaffMemberResponse> addStaffMember(@RequestBody StaffMemberDto staffMemberDto) {
        try{
           StaffMemberDto savedMemberDeatils= staffMemberService.addStaffMember(staffMemberDto);
           return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<StaffMemberDto>(savedMemberDeatils));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }

    }
}
