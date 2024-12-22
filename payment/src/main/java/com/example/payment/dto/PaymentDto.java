package com.example.payment.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private int paymentId;
    private int memberId;
    private String packageType;
    private String paymentDate;
    private String paymentTime;
    private boolean validity;
    private String expirayDate;
}
