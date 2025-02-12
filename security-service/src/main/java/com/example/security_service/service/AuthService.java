package com.example.security_service.service;


import com.example.emailSender.dto.SimpleMail;
import com.example.security_service.dto.MemberCredentialDto;
import com.example.security_service.dto.OtpUserDetails;
import com.example.security_service.dto.UserCredentialDto;
import com.example.security_service.entity.MemberCredentialsModel;
import com.example.security_service.entity.OtpUserDetailsModel;
import com.example.security_service.entity.UserCredentialsModel;
import com.example.security_service.repo.MemberCredentialsRepo;
import com.example.security_service.repo.OtpUserDetailsRepo;
import com.example.security_service.repo.UserCredentials;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@Service
public class AuthService {

    private final WebClient memberEmailWebClient;

    private final WebClient emailWebClient;

    @Autowired
    private OtpUserDetailsRepo otpUserDetailsRepo;

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

   public AuthService(WebClient MemberEmailWebClient,WebClient EmailWebClient) {
       this.memberEmailWebClient = MemberEmailWebClient;
       this.emailWebClient = EmailWebClient;
   }

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

    @Transactional(rollbackFor = Exception.class)
    public void forgetPassword(OtpUserDetails otpUserDetails){
       System.out.println("Inside the forgot service");
       try {
           boolean isEmailValid = isEmailValid(otpUserDetails.getUserName(), otpUserDetails.getEmail());
           System.out.println("is email valid : "+isEmailValid);
           if (!isEmailValid) {
               throw new RuntimeException("Email is not valid");
           }
           String[] generetedOtpArray = OtpGenerator.generateOtp();
           System.out.println("Generated otp : "+generetedOtpArray[0]);
           OtpUserDetailsModel otpUserModel = genereateNewOtpUserModel(otpUserDetails.getEmail(), generetedOtpArray[0], generetedOtpArray[1]);
           otpUserDetailsRepo.save(otpUserModel);
           sendEmail(otpUserModel.getEmail(),generetedOtpArray[0]);
           System.out.println("After sending the mail");
       } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
       }
    }
    public OtpUserDetailsModel genereateNewOtpUserModel(String email,String otp,String expirayTime){
        OtpUserDetailsModel newOtpUserDetails=new OtpUserDetailsModel();
        newOtpUserDetails.setEmail(email);
        newOtpUserDetails.setOtp(otp);
        newOtpUserDetails.setExpirayTime(expirayTime);
        return newOtpUserDetails;
    }

    public void sendEmail(String email,String otp){
        System.out.println("Before sending the mail");
        SimpleMail simpleMail=new SimpleMail();
        simpleMail.setReciver(email);
        simpleMail.setSubject("Your Otp is Here From MotionZone");
        simpleMail.setBody("Your otp is: " +otp +" The otp will only valid for 5 minutes");
        ResponseEntity<String> emailInfo=emailWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/simple").build())
                .bodyValue(simpleMail)
                .retrieve()
                .toEntity(String.class)
                .block();

        assert emailInfo != null;
        System.out.println(emailInfo.getBody());
        if(emailInfo.getStatusCode()!=HttpStatus.OK){
            throw new RuntimeException("Email not send");
        }
    }

    public boolean isEmailValid(String name,String email){
       try{
           System.out.println("inside the email checking service");
           ResponseEntity<String> emailInfo=memberEmailWebClient.get()
                   .uri(uriBuilder -> uriBuilder.path("/{firstName}/email").build(name))
                   .retrieve()
                   .onStatus(HttpStatusCode::is4xxClientError, response -> {
                       System.out.println("Client error occurred: " + response.statusCode());
                       return response.bodyToMono(String.class)
                               .flatMap(errorMessage -> {
                                   throw new RuntimeException(errorMessage);
                               });
                   })
                   .onStatus(HttpStatusCode::is5xxServerError, response -> {
                       System.out.println("Server error occurred: " + response.statusCode());
                       return response.createException().flatMap(Mono::error);
                   })
                   .toEntity(String.class)
                   .block();
           System.out.println("after sending the mail");
           assert emailInfo!=null;
           System.out.println("email is :"+emailInfo.getBody());
           if(emailInfo.getStatusCode()!=HttpStatus.OK){
               System.out.println("this says: "+emailInfo);
               return false;
           }
           if(!email.equals(emailInfo.getBody())){
               System.out.println("Email is not matching...");
               return false;
           }
           return true;
       }catch (WebClientResponseException e) {
           // Handle specific WebClient exceptions
           System.out.println("WebClient error: " + e.getResponseBodyAsString());
           throw new RuntimeException(e.getResponseBodyAsString());
       }
       catch(Exception e){
           System.out.println(e.getMessage());
           System.out.println(e);
           throw new RuntimeException(e.getMessage());
       }

    }

    public HashMap<Boolean,String> isOtpValid(String email, String otp){
       HashMap<Boolean, String> otpMap=new HashMap<>();
       try{
           OtpUserDetailsModel otpUserDetailsModel=otpUserDetailsRepo.findByEmail(email);
           if(otpUserDetailsModel==null){
               System.out.println("email not found");
               otpMap.put(false,"email not found");
               return otpMap;
           }
           else if(!otpUserDetailsModel.getOtp().equals(otp)){
               System.out.println("otp is not correct");
               otpMap.put(false,"otp is not correct");
               return otpMap;
           }
           else if(!validateOtpExpirayTime(otpUserDetailsModel.getExpirayTime())){
               System.out.println("Otp time has expired");
               otpMap.put(false,"Otp time has expired");
               return otpMap;
           }
           otpMap.put(true,"otp is validated");
           otpUserDetailsRepo.deleteOtpRecord(email);
           return otpMap;
       }catch(Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }

    public boolean validateOtpExpirayTime(String expirayTimeStr){
       try{
           long expirayTimeStamp=Long.parseLong(expirayTimeStr);

           LocalDateTime expirayTime= Instant.ofEpochMilli(expirayTimeStamp)
                   .atZone(ZoneId.systemDefault())
                   .toLocalDateTime();

           LocalDateTime now=LocalDateTime.now();

           if(now.isAfter(expirayTime)){
               return false;
           }
           return true;
       }catch(Exception e){
           throw new RuntimeException("Error validating expiray time");
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
