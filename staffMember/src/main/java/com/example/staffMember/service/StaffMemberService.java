package com.example.staffMember.service;



import com.example.staffMember.dto.StaffMemberCredentialDto;
import com.example.staffMember.dto.StaffMemberDto;
import com.example.staffMember.dto.StaffMemberTableDetails;
import com.example.staffMember.model.StaffMemberModel;
import com.example.staffMember.repo.StaffMemberRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
public class StaffMemberService {
    @Autowired
    private final WebClient staffMemberCredentialsWebClient;

    @Autowired
    private StaffMemberRepo staffMemberRepo;

    @Autowired
    private ModelMapper modelMapper;

    public StaffMemberService(StaffMemberRepo staffMemberRepo,ModelMapper modelMapper,WebClient StaffMemberCredentialsWebClient){
        this.staffMemberCredentialsWebClient = StaffMemberCredentialsWebClient;
        this.modelMapper = modelMapper;
        this.staffMemberRepo = staffMemberRepo;
    }

    @Transactional(rollbackFor = Exception.class)
    public StaffMemberDto addStaffMember(StaffMemberDto staffMemberDto) {

        try{
            if(isNamePresent(staffMemberDto.getFirstName())==1){
                throw new RuntimeException("Name already exists");
            }
            if(isEmailPresent(staffMemberDto.getEmail())==1){
                throw new RuntimeException("Email already exists");
            }
            if(isNumberPresent(staffMemberDto.getContactNumber())==1){
                throw new RuntimeException("Contact number already exists");
            }


            StaffMemberModel staffMemberModel = modelMapper.map(staffMemberDto, StaffMemberModel.class);
            StaffMemberModel savedStaffMember=staffMemberRepo.save(staffMemberModel);

            assert savedStaffMember != null;

            if(staffMemberDto.getPassword() !=null && staffMemberDto.getPosition().equals("Admin")){

                StaffMemberCredentialDto staffMemberCredentialDto=new StaffMemberCredentialDto();

                staffMemberCredentialDto.setPassword(staffMemberDto.getPassword());
                staffMemberCredentialDto.setUserId(savedStaffMember.getMemberId());
                staffMemberCredentialDto.setUserName(savedStaffMember.getFirstName());

                String credentialResponse=staffMemberCredentialsWebClient.post()
                        .bodyValue(staffMemberCredentialDto)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                assert credentialResponse != null;
                System.out.println(credentialResponse);
            }

            System.out.println(savedStaffMember);

            return modelMapper.map(savedStaffMember, StaffMemberDto.class);


        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<StaffMemberTableDetails> getStaffMemberTableDetails() {
        try{
            List <StaffMemberModel> staffMemberModelTableData=staffMemberRepo.findTableDetailsStaffMember();
            System.out.println(staffMemberModelTableData);
            return modelMapper.map(staffMemberModelTableData,new TypeToken<List<StaffMemberTableDetails>>(){}.getType());
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public StaffMemberDto getStaffMemberDetails(int staffMemberId) {
        try{
           StaffMemberModel staffMemberDetails=staffMemberRepo.findStaffMemberModelById(staffMemberId);
           return modelMapper.map(staffMemberDetails, StaffMemberDto.class);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer getInstructorCount(String position){
        try{
            return staffMemberRepo.getCountByPosition(position);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Internal Server Error");
        }
    }

    public Integer isNamePresent(String userName){
        return staffMemberRepo.memberExistsByName(userName);
    }

    public Integer isEmailPresent(String email){
        return staffMemberRepo.memberExistsByEmail(email);
    }

    public Integer isNumberPresent(String number){
        return staffMemberRepo.memberExistsByContactNumber(number);
    }

}
