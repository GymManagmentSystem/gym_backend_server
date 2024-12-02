package com.example.security_service.service;


import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.UserCredentialsModel;
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

    public String generateToken(String userName){
        System.out.println(userName);
        return jwtService.generateToken(userName);

    }

    public void validateToken(String token){
        jwtService.vlidateToken(token);
    }

}
