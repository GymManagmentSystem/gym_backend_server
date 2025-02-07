package com.example.security_service.repo;

import com.example.security_service.entity.MemberCredentialsModel;
import com.example.security_service.entity.OtpUserDetailsModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpUserDetailsRepo extends JpaRepository<OtpUserDetailsModel,String> {
    @Query(value = "SELECT * FROM otp_user_details_model WHERE email=?1",nativeQuery = true)
    OtpUserDetailsModel findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM otp_user_details_model WHERE email=:email",nativeQuery = true)
    void deleteOtpRecord(@Param("email") String email);

}
