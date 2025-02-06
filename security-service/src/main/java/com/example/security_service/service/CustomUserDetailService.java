package com.example.security_service.service;

import com.example.security_service.dto.CustomMemberDetails;
import com.example.security_service.dto.CustomUserDetails;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.MemberCredentialsModel;
import com.example.security_service.entity.UserCredentialsModel;
import com.example.security_service.repo.MemberCredentialsRepo;
import com.example.security_service.repo.UserCredentials;
import com.example.security_service.utility.UserContext;
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
    private MemberCredentialsRepo memberCredentialsRepo;

    @Autowired
    private ModelMapper modelMapper;



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String userType= UserContext.getUserType();
        System.out.println("user name is " + userName);
        try{
            if("STAFF".equals(userType)){
                UserCredentialsModel userCredentialModel=userCredentialsRepository.findByUserName(userName);
                return modelMapper.map(userCredentialModel, CustomUserDetails.class);
            }
            else if("MEMBER".equals(userType)){
                MemberCredentialsModel memberCredentialsModel=memberCredentialsRepo.findByUserName(userName);
                return modelMapper.map(memberCredentialsModel, CustomMemberDetails.class);
            }
            else{
                throw new UsernameNotFoundException("Role not found");
            }
        }catch(Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }

    }
}
