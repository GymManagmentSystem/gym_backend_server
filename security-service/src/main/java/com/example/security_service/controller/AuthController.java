package com.example.security_service.controller;


import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/auth")

public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public UserCredentialDto addNewUserCredentials(@RequestBody UserCredentialDto userCredentialDto) {
        return authService.addNewUserCredentials(userCredentialDto);
    }


    @PostMapping("/token")
   public String getToken(@RequestBody UserCredentialDto userCredentialDto) {
        System.out.println("Before Authentication");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentialDto.getUserName(), userCredentialDto.getPassword()));
            System.out.println("After authentication " + authentication);
            if (!authentication.isAuthenticated()) {
                throw new UsernameNotFoundException("Invalid username or password");
            }
            return authService.generateToken(userCredentialDto.getUserName());
        }catch(BadCredentialsException e) {
            return "Invalid username or password";
        }
        catch(Exception e) {
            System.out.println(e.getClass().getName());
            return "Internal Server Error";
        }

   }

   @GetMapping("/validate")
   public String validateToken(@RequestParam("token") String token) {
        System.out.println(token);
        authService.validateToken(token);
        return "Token validated";
   }


}
