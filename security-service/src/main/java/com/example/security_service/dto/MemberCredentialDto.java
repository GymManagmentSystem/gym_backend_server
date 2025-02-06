package com.example.security_service.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCredentialDto {
    @Id
    int memberId;
    String userName;
    String password;
    boolean isFirstUser;
}
