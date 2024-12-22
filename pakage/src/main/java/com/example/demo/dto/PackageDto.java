package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDto {
    private Integer packageId;
    private String packageName;
    private String packageDescription;
    private Integer packageValidTime;
    private Integer packageAmount;
}
