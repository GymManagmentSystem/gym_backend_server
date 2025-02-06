package com.example.security_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialDto {
    private int userId;
    private String userName;
    private String password;
    private String userType;

}
