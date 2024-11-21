package com.example.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private int memberId;
    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private String gender;
    private String contactNumber;
    private String email;
    private String dateRegistered;
    private int weight;
    private int height;
}
