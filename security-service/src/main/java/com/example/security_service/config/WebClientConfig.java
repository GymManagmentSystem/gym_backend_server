package com.example.security_service.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {return WebClient.builder();}

    @Bean
    public WebClient MemberEmailWebClient(){
        return webClientBuilder().baseUrl("http://apigateway/api/v1/members").build();
    }

    @Bean
    public WebClient EmailWebClient(){
        return webClientBuilder().baseUrl("http://apigateway/api/v1/email").build();
    }

}
