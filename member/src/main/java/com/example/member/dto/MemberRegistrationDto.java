package com.example.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegistrationDto {
    private int memberId;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String address;
    private String contactNumber;
    private String dateRegistered;
    private int weight;
    private int height;
    private String gender;

    private String packageType;
    private String paymentDate;
    private String paymentTime;
    private int amount;
    private boolean paymentValidity;
}
