package com.example.security_service.service;


import com.example.security_service.dto.MemberCredentialDto;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.MemberCredentialsModel;
import com.example.security_service.entity.UserCredentialsModel;
import com.example.security_service.repo.MemberCredentialsRepo;
import com.example.security_service.repo.UserCredentials;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserCredentials userCredentialsRepo;

   @Autowired
   private ModelMapper modelMapper;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private MemberCredentialsRepo memberCredentialsRepo;


    public UserCredentialDto addNewUserCredentials(UserCredentialDto userCredentialDto) {
        userCredentialDto.setPassword(passwordEncoder.encode(userCredentialDto.getPassword()));
        UserCredentialsModel userCredentialsModel = modelMapper.map(userCredentialDto, UserCredentialsModel.class);
        try{
            UserCredentialsModel savedUserCredentialModel=userCredentialsRepo.save(userCredentialsModel);
            return modelMapper.map(savedUserCredentialModel, UserCredentialDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public MemberCredentialsModel addNewMemberCredentials(MemberCredentialDto memberCredentialDto) {
        memberCredentialDto.setPassword(passwordEncoder.encode(memberCredentialDto.getPassword()));
        MemberCredentialsModel memberCredentialsModel = modelMapper.map(memberCredentialDto, MemberCredentialsModel.class);
        try{
            MemberCredentialsModel savedMemberCredentialModel=memberCredentialsRepo.save(memberCredentialsModel);
            return modelMapper.map(savedMemberCredentialModel, MemberCredentialsModel.class);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public void resetPassword(MemberCredentialDto memberCredentialDto) {
        memberCredentialDto.setPassword(passwordEncoder.encode(memberCredentialDto.getPassword()));
        System.out.println(memberCredentialDto.getPassword());
        System.out.println(memberCredentialDto.getUserName());
        memberCredentialsRepo.resetPassword(memberCredentialDto.getPassword(),memberCredentialDto.getUserName());
    }

    public String generateToken(String userName){
        System.out.println(userName);
        return jwtService.generateToken(userName);

    }

    public void validateToken(String token){
        jwtService.vlidateToken(token);
    }

}
