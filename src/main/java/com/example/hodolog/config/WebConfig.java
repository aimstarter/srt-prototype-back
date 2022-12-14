package com.example.hodolog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8080", "http://172.17.0.1:8081")
                .allowedOrigins("http://192.168.1.10:8081", "http://172.17.0.1:8081", "http://localhost:8080")
                //.allowedOrigins("*")
                .allowedMethods("OPTIONS", "GET", "POST")
                .allowCredentials(true);
    }
}
