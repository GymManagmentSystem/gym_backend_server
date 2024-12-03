package com.example.staffMember.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffMemberCredentialDto {
    private int userId;
    private String userName;
    private String password;
}
