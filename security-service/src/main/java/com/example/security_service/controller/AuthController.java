package com.example.security_service.controller;


import com.example.security_service.customResponse.ErrorResponse;
import com.example.security_service.customResponse.ResponseClass;
import com.example.security_service.customResponse.SuccessResponse;
import com.example.security_service.dto.CustomMemberDetails;
import com.example.security_service.dto.MemberCredentialDto;
import com.example.security_service.dto.OtpUserDetails;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.OtpUserDetailsModel;
import com.example.security_service.service.AuthService;
import com.example.security_service.service.OtpGenerator;
import com.example.security_service.service.PasswordGenerator;
import com.example.security_service.utility.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(value = "api/v1/auth")

public class AuthController {
    @Autowired
    private AuthService authService;


    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/member/new-password")
    public ResponseEntity<String> generateNewMemberCredentials(@RequestParam int userId, @RequestParam String userName) {
        try{
            System.out.println("user id is : " + userId);

            PasswordGenerator passwordGenerator=new PasswordGenerator();
            String generatedPassword=passwordGenerator.generatePassword();

            MemberCredentialDto newMemberCredentialDto=new MemberCredentialDto();

            newMemberCredentialDto.setPassword(generatedPassword);
            newMemberCredentialDto.setMemberId(userId);
            newMemberCredentialDto.setUserName(userName);
            newMemberCredentialDto.setFirstUser(true);

            System.out.println(newMemberCredentialDto.getMemberId());

            authService.addNewMemberCredentials(newMemberCredentialDto);

            return ResponseEntity.status(HttpStatus.OK).body(generatedPassword);
        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/member/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody MemberCredentialDto memberCredentialDto) {
        try{
            authService.resetPassword(memberCredentialDto);
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successful");
        }catch(Exception e){
            System.out.println("Password reset failed");
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password reset failed");
        }
    }

    @PostMapping("/member/forgot-password")
    public ResponseEntity<String> sendEmail(@RequestBody OtpUserDetails otpUserDetails) {
        System.out.println("inside the forgot password controller");
        try{
            authService.forgetPassword(otpUserDetails);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @PostMapping("/register")
    public String addNewUserCredentials(@RequestBody UserCredentialDto userCredentialDto) {
        try{
            authService.addNewUserCredentials(userCredentialDto);
            return  "User credential added successfully";

        }catch(Exception e){
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<ResponseClass> getToken(@RequestBody UserCredentialDto userCredentialDto) {
        System.out.println("Before Authentication");
        UserContext.setUserType(userCredentialDto.getUserType());// set the user type to member or staff in thread level
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentialDto.getUserName(), userCredentialDto.getPassword()));
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid username or password");
            }
            System.out.println("Authentication Successfull");
            String token=authService.generateToken(userCredentialDto.getUserName());
            if(userCredentialDto.getUserType().equals("MEMBER")){
                CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
                // Extract individual values
                String isFirstUser = userDetails.isFirstUser()?"New Member":"Existing Member";
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("MemberType: "+isFirstUser,token));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("login Successful",token));


        }catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password"));
        }
        catch(Exception e) {
            System.out.println(e.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal Server Error"));
        }

   }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        System.out.println(token);
        authService.validateToken(token);
        return "Token validated";
   }


}
