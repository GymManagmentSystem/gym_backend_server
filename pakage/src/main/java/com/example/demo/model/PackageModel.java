package com.example.demo.model;

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
public class PackageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageId;
    private String packageName;
    private String packageDescription;
    private Integer packageValidTime;
    private Integer packageAmount;
}
