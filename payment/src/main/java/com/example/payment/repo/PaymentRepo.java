package com.example.payment.repo;

import com.example.payment.dto.PaymentMonthDto;
import com.example.payment.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepo extends JpaRepository <PaymentModel,Integer> {
    @Query(value = "SELECT payment_id,member_id,package_type,payment_date,payment_time,validity,expiray_date FROM payment_model WHERE member_id=?1",nativeQuery = true)
    List<PaymentModel> getAllPaymentDetailsById(Integer memberId);

    @Modifying
    @Query(value = "UPDATE payment_model SET validity=false WHERE validity=true AND member_id=?1",nativeQuery = true)
    Integer invalidatePastPayment(Integer memberId);

    @Query(value = "SELECT * FROM payment_model ORDER BY payment_id DESC LIMIT 1",nativeQuery = true)
    PaymentModel getLatestPayment();

    @Query(value = "SELECT COUNT(member_id) FROM payment_model WHERE expiray_date>=current_date() AND validity=true",nativeQuery = true)
    Integer getCurrentMemberCount();

    @Query(value = "SELECT COUNT(member_id) FROM payment_model WHERE expiray_date<current_date() AND validity=true",nativeQuery = true)
    Integer getExpiredMemberCount();

    @Query(value = "SELECT payment_id,member_id,package_type,payment_date,payment_time,validity,expiray_date FROM payment_model WHERE validity=true",nativeQuery = true)
    List<PaymentModel> getLatestPayments();

    @Query(value = "SELECT COUNT(member_id) AS memberCount,MONTHNAME(payment_date) AS month,YEAR(payment_date) AS year FROM payment_model WHERE package_type=?1 GROUP BY month,year ORDER BY year DESC,MONTH(month) DESC LIMIT 7",nativeQuery = true)
    List<Object []> getMonthlySpecificPackageCount(String packageType);

    @Query(value="SELECT SUM(payment_amount) as amount,YEAR(payment_date)as year,MONTHNAME(payment_date) as month from payment_model  GROUP BY year,month ORDER BY year DESC,MONTH(month) DESC LIMIT 10",nativeQuery = true)
    List<Object []> getMonthlyIncome();

}
