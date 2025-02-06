package com.example.security_service.repo;

import com.example.security_service.entity.MemberCredentialsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberCredentialsRepo extends JpaRepository<MemberCredentialsModel,Integer> {
    @Query(value = "SELECT user_name,password,member_id,is_first_user from member_credentials_model WHERE user_name=?1",nativeQuery = true)
    MemberCredentialsModel findByUserName(String userName);
}
