package com.example.member.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";

    public String saveProfileImage(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Automatically create it if not exists
        }


        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + File.separator + fileName;

        File destination=new File(filePath);

        file.transferTo(destination);

        return "http://localhost:8080/uploads/" + fileName;
    }

}
