package com.example.security_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpUserDetailsModel {
    @Id
    private String email;
    private String otp;
    private String expirayTime;
}
