package com.example.demo.service;

import com.example.demo.customResponse.ErrorResponse;
import com.example.demo.customResponse.PackageResponse;
import com.example.demo.customResponse.SuccessResponse;
import com.example.demo.dto.PackageDto;
import com.example.demo.model.PackageModel;
import com.example.demo.repo.PackageRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {
    @Autowired
    private PackageRepo packageRepo;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<PackageResponse> getAllPackages() {
        try{
            List<PackageModel> packageModelList = packageRepo.findAll();
            List<PackageDto> packageDtoList=modelMapper.map(packageModelList,new TypeToken<List<PackageDto>>(){}.getType());
            return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<PackageDto>(packageDtoList));
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage())) ;
        }

    }

    public ResponseEntity<PackageResponse> addNewPackage(PackageDto packageDto) {
        try{
            String packageName=packageDto.getPackageName();
            Integer PackageNameExist=packageRepo.existByName(packageName);
            if(PackageNameExist==0){
                packageRepo.save(modelMapper.map(packageDto,PackageModel.class));
                PackageDto savedPackage=modelMapper.map(packageRepo.getLastExercise(),PackageDto.class);
                return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<PackageDto>(savedPackage));
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Package already exists"));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }
}
