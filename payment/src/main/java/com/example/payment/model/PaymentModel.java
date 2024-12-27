package com.example.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;
    private int memberId;
    @Column(nullable = false)
    private String packageType;
    private String paymentDate;
    private String paymentTime;
    private Boolean validity;
    private String expirayDate;
    private int paymentAmount;
}
