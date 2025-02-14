package com.example.member.service;


import com.example.emailSender.dto.SimpleMail;
import com.example.member.dto.BasicMemberDto;
import com.example.member.dto.MemberDetailsDto;
import com.example.member.dto.MemberDto;
import com.example.member.dto.MemberRegistrationDto;
import com.example.member.model.MemberModel;
import com.example.member.repo.MemberRepo;
import com.example.payment.customResponse.PaymentClassResponse;
import com.example.payment.dto.PaymentDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class MemberService {

    private final WebClient paymentWebClient;

    private final WebClient memberAuthWebClient;

    private final WebClient emailWebClient;

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private ModelMapper modelMapper;

    public MemberService(WebClient PaymentWebClient,WebClient MemberAuthWebClient,WebClient EmailWebClient,MemberRepo memberRepo,ModelMapper modelMapper) {
        this.paymentWebClient = PaymentWebClient;
        this.memberAuthWebClient = MemberAuthWebClient;
        this.emailWebClient = EmailWebClient;
        this.memberRepo = memberRepo;
        this.modelMapper = modelMapper;
    }

    public List<BasicMemberDto> getAllMembers(){
        try{
            List<MemberModel> memberDetails=memberRepo.getAllMembersDetails();
            System.out.println(memberDetails);
            return modelMapper.map(memberDetails,new TypeToken<List<BasicMemberDto>>(){}.getType());
        }catch(Exception e){
            throw new RuntimeException("Internal Server Error");
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public MemberRegistrationDto addNewMember(MemberDto memberDto, PaymentDto paymentDto,String jwtToken) {
        String name = memberDto.getFirstName();
        String email = memberDto.getEmail();
        String mobileNumber = memberDto.getContactNumber();
        try {
            if (memberRepo.memberExistsByName(name) == 1) {
                throw new RuntimeException("NE"); //name already exists

            }

            if (memberRepo.memberExistsByEmail(email) == 1) {
                throw new RuntimeException("EE");//email exists
            }

            if (memberRepo.memberExistsByContactNumber(mobileNumber) == 1) {
                throw new RuntimeException("NE");//number exists
            }

            MemberModel savedMember=memberRepo.save(modelMapper.map(memberDto, MemberModel.class));
            MemberDto latestMember = modelMapper.map(savedMember,MemberDto.class);
            System.out.println("latest member is :"+latestMember);
            paymentDto.setMemberId(latestMember.getMemberId());

            ResponseEntity<PaymentClassResponse<PaymentDto>> paymentInfo = paymentWebClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/{memberId}").build(latestMember.getMemberId()))
                    .headers(headers -> headers.setBearerAuth(jwtToken))
                    .bodyValue(paymentDto)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<PaymentClassResponse<PaymentDto>>() {})
                    .block();

            assert paymentInfo != null;


            if(paymentInfo.getStatusCode()!=HttpStatus.OK){
                throw new RuntimeException("PF");
            }

            PaymentDto paymentDetails = modelMapper.map(paymentInfo.getBody().getData(),PaymentDto.class);
            MemberRegistrationDto latestMemberDetails=new MemberRegistrationDto();
            modelMapper.map(paymentDetails,latestMemberDetails);
            modelMapper.map(latestMember,latestMemberDetails);
            System.out.println("saved member details "+latestMemberDetails);


            ResponseEntity<String> memberAuthInfo=memberAuthWebClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/new-password")
                            .queryParam("userId",latestMemberDetails.getMemberId())
                            .queryParam("userName",latestMemberDetails.getFirstName())
                            .build())
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            assert memberAuthInfo != null;

            if(memberAuthInfo.getStatusCode()!=HttpStatus.OK){
                throw new RuntimeException("AF");
            }


            SimpleMail simpleMail=new SimpleMail();
            simpleMail.setReciver(latestMemberDetails.getEmail());
            simpleMail.setSubject("Welcome! You have created your MotionZone Account");
            simpleMail.setBody("Your password is: "+memberAuthInfo.getBody()+"You must reset password at first login");
            ResponseEntity<String> emailInfo=emailWebClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/simple").build())
                    .bodyValue(simpleMail)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            assert emailInfo != null;

            if(emailInfo.getStatusCode()!=HttpStatus.OK){
                throw new RuntimeException("EF");
            }

            return latestMemberDetails;

        }
        catch(WebClientResponseException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("PF");//payment Failed
        }
        catch(DataIntegrityViolationException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("DIVE");//dataIntegrityViolationException
        }
        catch(WebClientRequestException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("RF");//request failed
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public MemberDetailsDto getMemberDetails(Integer memberId,String jwtToken) {

        try{
            boolean memeberExist=memberRepo.findById(memberId).isPresent();
            if(!memeberExist){
                System.out.println("Inside if block");
                throw new RuntimeException("MNF");//Member Not Found
            }
            MemberModel memberModel=memberRepo.findById(memberId).get();
            assert memberModel != null;
            MemberDto memberDto=modelMapper.map(memberModel,MemberDto.class);

            ResponseEntity<PaymentClassResponse<PaymentDto>>  paymentList = paymentWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/{memberId}").build(memberId))
                    .headers(headers -> headers.setBearerAuth(jwtToken))
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<PaymentClassResponse<PaymentDto>>() {})
                    .block();

            assert paymentList != null;

            if(paymentList.getStatusCode()!=HttpStatus.OK){
                throw new RuntimeException("ISE");//Internal Server Error
            }
            MemberDetailsDto memberDetails=new MemberDetailsDto();
            memberDetails.setMember(memberDto);
            memberDetails.setPayments(modelMapper.map(paymentList.getBody().getDataList(),new TypeToken<List<PaymentDto>>(){}.getType()));
            return memberDetails;

        }catch(NoSuchElementException e){
            throw new RuntimeException("MNF");//Member Not Found
        }catch(WebClientRequestException e){
            throw new RuntimeException("RF");
        } catch (Exception e){
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            throw new RuntimeException("IS");
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public MemberDto updateMember(MemberDto memberDto) {
        String name = memberDto.getFirstName();
        String email = memberDto.getEmail();
        String mobileNumber = memberDto.getContactNumber();
        MemberModel memberModel=modelMapper.map(memberDto,MemberModel.class);
        Integer isMemberUpdated=0;
        try {
            Optional<MemberModel> pastMemberDetails=memberRepo.findById(memberDto.getMemberId());
            boolean isPresentMember=pastMemberDetails.isPresent();
            if(!isPresentMember){
               throw new RuntimeException("NUMF");
            }
            if(pastMemberDetails.get().getFirstName().equals(name) && pastMemberDetails.get().getEmail().equals(email) && pastMemberDetails.get().getContactNumber().equals(mobileNumber)){
                isMemberUpdated=memberRepo.updateMemberDetails(memberModel);
                System.out.println(memberModel.getMemberId());
            }
            else{
                if (memberRepo.memberExistsByName(name) == 1) {
                    throw new RuntimeException("NE"); //name already exists
                }

                if (memberRepo.memberExistsByEmail(email) == 1) {
                    throw new RuntimeException("EE");//email exists
                }

                if (memberRepo.memberExistsByContactNumber(mobileNumber) == 1) {
                    throw new RuntimeException("NE");//number exists
                }
                isMemberUpdated=memberRepo.updateMemberDetails(memberModel);
                System.out.println(memberModel.getMemberId());
            }

            if(isMemberUpdated==0){
                throw new RuntimeException("MUF");//Member update failed
            }
            MemberModel updatedMember=memberRepo.findById(memberDto.getMemberId()).get();
            return modelMapper.map(updatedMember,MemberDto.class);

        }catch(NoSuchElementException e){
            throw new RuntimeException("NUMF");//No updated Member Found
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());//Internal Server Error
        }


    }

    public String getMemberEmail(String firstName){
        System.out.println("Inside getMemberEmail");
        return memberRepo.getMemberEmail(firstName);
    }

    public Integer isMemberExists(String firstName){
        return memberRepo.memberExistsByName(firstName);
    }

}
