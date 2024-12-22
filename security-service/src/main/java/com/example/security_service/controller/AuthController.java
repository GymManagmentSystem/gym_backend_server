package com.example.security_service.controller;


import com.example.security_service.customResponse.ErrorResponse;
import com.example.security_service.customResponse.ResponseClass;
import com.example.security_service.customResponse.SuccessResponse;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.service.AuthService;
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
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentialDto.getUserName(), userCredentialDto.getPassword()));
            System.out.println("After authentication " + authentication);
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid username or password");
            }
            System.out.println("Authentication Successfull");
            String token=authService.generateToken(userCredentialDto.getUserName());
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
