package com.example.member.dto;

import com.example.payment.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailsDto {
    private MemberDto member;
    private List<PaymentDto> payments;
}
