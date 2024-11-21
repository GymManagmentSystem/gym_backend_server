package com.example.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient PaymentWebClient(){
        return WebClient.builder().baseUrl("http://localhost:8083/api/v1/payments").build();
    }
}
