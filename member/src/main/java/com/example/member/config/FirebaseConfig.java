package com.example.member.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        String serviceAccountPath=System.getProperty("user.dir")+"/member"+"/uploadimage-8a752-firebase-adminsdk-ae50l-6a905487a6.json";
        FileInputStream serviceAccountStream=new FileInputStream(serviceAccountPath);

        FirebaseOptions options=FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setStorageBucket("uploadimage-8a752.appspot.com")
                .build();

        return FirebaseApp.initializeApp(options);
    }

}
