package com.example.emailSender.controller;


import com.example.emailSender.dto.SimpleMail;
import com.example.emailSender.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/v1/email")
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/simple")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody SimpleMail simpleMailDto) {
        try{
            System.out.println("inside the contorller");
            System.out.println(simpleMailDto);
            emailSenderService.sendSimpleMail(simpleMailDto);
            return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
