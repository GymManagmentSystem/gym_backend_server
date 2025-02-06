package com.example.security_service.service;


import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordGenerator {
    public String generatePassword(){
        SecureRandom random=new SecureRandom();
        byte[] bytes=new byte[10];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
