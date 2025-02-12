package com.example.security_service.repo;

import com.example.security_service.entity.MemberCredentialsModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCredentialsRepo extends JpaRepository<MemberCredentialsModel,Integer> {
    @Query(value = "SELECT user_name,password,member_id,is_first_user from member_credentials_model WHERE user_name=?1",nativeQuery = true)
    MemberCredentialsModel findByUserName(String userName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE member_credentials_model SET password=:password,is_first_user=false WHERE user_name=:userName",nativeQuery = true)
    Integer resetPassword(@Param("password") String password,@Param("userName") String userName);

}
