package com.example.staffMember.controller;


import com.example.staffMember.customResponse.ErrorResponse;
import com.example.staffMember.customResponse.StaffMemberResponse;
import com.example.staffMember.customResponse.SuccessResponse;
import com.example.staffMember.dto.StaffMemberDto;
import com.example.staffMember.dto.StaffMemberTableDetails;
import com.example.staffMember.service.StaffMemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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

    @GetMapping("/")
    public ResponseEntity<StaffMemberResponse> getStaffMemberDetailsForTable() {
        try{
            List<StaffMemberTableDetails> staffMemberTableDetails= staffMemberService.getStaffMemberTableDetails();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<StaffMemberTableDetails>(staffMemberTableDetails));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{staffMemberId}")
    public ResponseEntity<StaffMemberResponse> getStaffMemberById(@PathVariable("staffMemberId") int staffMemberId) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<StaffMemberDto>(staffMemberService.getStaffMemberDetails(staffMemberId)));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{position}/count")
    public ResponseEntity<StaffMemberResponse> getInstrctorsCount(@PathVariable("position") String position) {
        try{
            Integer posotionCount=staffMemberService.getInstructorCount(position);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<Integer>(posotionCount));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }
    }
}
