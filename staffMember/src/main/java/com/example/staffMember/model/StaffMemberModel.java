package com.example.staffMember.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffMemberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
