package com.example.emailSender;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailSenderApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		// Set environment properties for Spring Boot to use
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));

		SpringApplication.run(EmailSenderApplication.class, args);
	}

}
