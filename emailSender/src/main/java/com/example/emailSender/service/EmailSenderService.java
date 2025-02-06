package com.example.emailSender.service;

import com.example.emailSender.dto.SimpleMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendSimpleMail(SimpleMail simpleMail) {
        System.out.println("inside the service");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(simpleMail.getReciver());
        simpleMailMessage.setFrom("Motion Zone");
        simpleMailMessage.setSubject(simpleMail.getSubject());
        simpleMailMessage.setText(simpleMail.getBody());
        mailSender.send(simpleMailMessage);
    }

}
