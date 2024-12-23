package com.example.staffMember.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffMemberTableDetails {
    private int memberId;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String gender;
    private String registeredDate;
}
