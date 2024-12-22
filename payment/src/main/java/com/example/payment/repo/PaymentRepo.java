package com.example.payment.repo;

import com.example.payment.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepo extends JpaRepository <PaymentModel,Integer> {
    @Query(value = "SELECT payment_id,member_id,package_type,payment_date,payment_time,validity,expiray_date FROM payment_model WHERE member_id=?1",nativeQuery = true)
    List<PaymentModel> getAllPaymentDetailsById(Integer memberId);

    @Query(value = "UPDATE payment_model SET validity=false WHERE validity=true AND member_id=?1",nativeQuery = true)
    Integer invalidatePastPayment(Integer memberId);

    @Query(value = "SELECT * FROM payment_model ORDER BY payment_id DESC LIMIT 1",nativeQuery = true)
    PaymentModel getLatestPayment();



}
