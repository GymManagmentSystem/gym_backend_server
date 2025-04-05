package com.example.member;

import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MemberApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		// Set env vars for Spring Boot to access
		System.setProperty("FIREBASE_CONFIG_PATH", dotenv.get("FIREBASE_CONFIG_PATH"));

		SpringApplication.run(MemberApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

}
