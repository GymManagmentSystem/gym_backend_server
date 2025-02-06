package com.example.security_service.repo;

import com.example.security_service.entity.MemberCredentialsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCredentialsRepo extends JpaRepository<MemberCredentialsModel,Integer> {
}
