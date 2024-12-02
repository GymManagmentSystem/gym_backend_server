package com.example.security_service.service;

import com.example.security_service.dto.CustomUserDetails;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.UserCredentialsModel;
import com.example.security_service.repo.UserCredentials;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserCredentials userCredentialsRepository;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("user name is " + userName);
        try{
            UserCredentialsModel userCrendtialModel=userCredentialsRepository.findByUserName(userName);
            return modelMapper.map(userCrendtialModel, CustomUserDetails.class);
        }catch(Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }

    }
}
