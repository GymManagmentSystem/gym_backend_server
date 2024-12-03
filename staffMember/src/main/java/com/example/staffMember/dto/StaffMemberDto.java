package com.example.staffMember.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffMemberDto {
    private int staffMemberId;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private int age;
    private String gender;
    private String address;
    private String position;
    private String registeredDate;
    private String qualifications;
    private String password;
}
