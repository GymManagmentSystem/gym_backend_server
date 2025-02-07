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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
        System.out.println("inside the email checking service");
        ResponseEntity<String> emailInfo=memberEmailWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{firstName}/email").build(name))
                .retrieve()
                .toEntity(String.class)
                .block();
        System.out.println("after sending the mail");
        assert emailInfo!=null;
        if(emailInfo.getStatusCode()!=HttpStatus.OK){
            System.out.println(emailInfo.getBody());
            return false;
        }
        if(!email.equals(emailInfo.getBody())){
            System.out.println("email given by user "+email);
            System.out.println("email given by db "+emailInfo.getBody());
            System.out.println("Entered email is not correct");
            return false;
        }
        return true;
    }


    public String generateToken(String userName){
        System.out.println(userName);
        return jwtService.generateToken(userName);

    }

    public void validateToken(String token){
        jwtService.vlidateToken(token);
    }

}
