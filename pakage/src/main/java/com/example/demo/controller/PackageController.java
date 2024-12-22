package com.example.demo.controller;

import com.example.demo.customResponse.PackageResponse;
import com.example.demo.dto.PackageDto;
import com.example.demo.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/packages")

public class PackageController {
    @Autowired
    private PackageService packageService;

    @GetMapping(value = "/")
    public ResponseEntity<PackageResponse> getAllPackages() {
        return packageService.getAllPackages();
    }

    @PostMapping(value="/")
    public ResponseEntity<PackageResponse> addNewPackage(@RequestBody PackageDto packageDto){
        return packageService.addNewPackage(packageDto);
    }
}
