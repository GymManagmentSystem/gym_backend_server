package com.example.security_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpUserDetails {
    private String userName;
    private String email;
    private String otp;
    private String expirayTime;
    private String operationType;
}
