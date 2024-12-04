package com.example.staffMember.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffMemberTableDetails {
    private int staffMemberId;
    private String firstName;
    private String contactNumber;
    private String gender;
}
