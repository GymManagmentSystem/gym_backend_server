package com.example.security_service.repo;

import com.example.security_service.entity.OtpUserDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpUserDetailsRepo extends JpaRepository<OtpUserDetailsModel,String> {
}
