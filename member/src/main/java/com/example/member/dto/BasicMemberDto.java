package com.example.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicMemberDto {
    private int memberId;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String dateRegistered;
    private String gender;
}
