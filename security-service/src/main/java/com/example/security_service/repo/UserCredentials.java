package com.example.security_service.repo;

import com.example.security_service.entity.UserCredentialsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserCredentials extends JpaRepository<UserCredentialsModel, Integer> {

    @Query(value = "SELECT user_name,password,user_id from user_credentials_model WHERE user_name=?1",nativeQuery = true)
    UserCredentialsModel findByUserName(String userName);
}
