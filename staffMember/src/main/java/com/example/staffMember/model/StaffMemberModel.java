package com.example.staffMember.model;


import jakarta.persistence.*;
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
    private int memberId;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String contactNumber;
    private String email;
    private int age;
    @Column(nullable = false)
    private String gender;
    private String address;
    private String position;
    private String registeredDate;
    private String qualifications;
}
