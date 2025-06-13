package com.example.userservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
//    @LoadBalanced
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://company-service:8081") // load-balanced URL
//                .baseUrl("http://localhost:8081") // load-balanced URL
                .build();
    }
}
