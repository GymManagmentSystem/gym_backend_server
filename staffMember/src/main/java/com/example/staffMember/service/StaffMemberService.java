package com.example.staffMember.service;



import com.example.staffMember.dto.StaffMemberCredentialDto;
import com.example.staffMember.dto.StaffMemberDto;
import com.example.staffMember.model.StaffMemberModel;
import com.example.staffMember.repo.StaffMemberRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
            StaffMemberModel staffMemberModel = modelMapper.map(staffMemberDto, StaffMemberModel.class);
            StaffMemberModel savedStaffMember=staffMemberRepo.save(staffMemberModel);
            assert savedStaffMember != null;
            if(staffMemberDto.getPassword() !=null && staffMemberDto.getPosition().equals("Admin")){
                StaffMemberCredentialDto staffMemberCredentialDto=new StaffMemberCredentialDto();

                staffMemberCredentialDto.setPassword(staffMemberDto.getPassword());
                staffMemberCredentialDto.setUserId(savedStaffMember.getStaffMemberId());
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

}
